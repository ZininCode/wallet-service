package se.vegas.tasknz.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.vegas.tasknz.dto.BalanceDto;
import se.vegas.tasknz.dto.TransactionHistoryDto;
import se.vegas.tasknz.exception.PlayerIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.repository.WalletRepository;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Date: 10.01.2024
 *
 * @author Nikolay Zinin
 */
@Slf4j
@Service
@AllArgsConstructor
public class WalletService {

    private final MappingService mappingService;
    private final WalletRepository walletRepository;

    public Wallet getWalletByPlayerId(String playerId) throws WalletNotFoundException {
        return walletRepository.findByPlayerId(playerId).orElseThrow(() -> {
            log.error(String.format("Player with id %s not found", playerId));
            return new WalletNotFoundException("Player with Id "+ playerId + " not found ");
        });
    }

    public BalanceDto getPlayerBalance(String playerId) throws WalletNotFoundException {
        Wallet wallet = getWalletByPlayerId(playerId);
        return mappingService.mapWalletToBalanceDto(wallet);
    }

    public List<TransactionHistoryDto> getAllTransactionsByPlayerId(String playerId) throws WalletNotFoundException {
        Wallet wallet = getWalletByPlayerId(playerId);
        return wallet.getWalletTransactions().stream()
                .map(new MappingService()::transactionToDto)
                .collect(Collectors.toList());
    }
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public void createWallet(String playerId) throws PlayerIdRedundantException {
        checkPlayerId(playerId);
        Wallet wallet = Wallet.builder()
                .playerId(playerId)
                .balance(BigDecimal.valueOf(0))
                .build();
        walletRepository.save(wallet);
    }
    @Transactional
    public void deleteWallet(String walletId) throws PlayerIdRedundantException {

        Wallet wallet = walletRepository.findById(Long.valueOf(walletId)).orElseThrow();
        walletRepository.delete(wallet);
    }

    private void checkPlayerId(String playerId) throws PlayerIdRedundantException {
        Optional<Wallet> wallet = walletRepository.findByPlayerId(playerId);
        if (wallet.isPresent()) {
            log.error(String.format("Wallet already exist for player with id %s", playerId));
            throw new PlayerIdRedundantException("Wallet already already exists for playerId " + playerId);
        }
    }
}
