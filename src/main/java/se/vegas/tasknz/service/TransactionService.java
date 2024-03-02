package se.vegas.tasknz.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.vegas.tasknz.dto.TransactionDTO;
import se.vegas.tasknz.exception.NotEnoughCreditException;
import se.vegas.tasknz.exception.TransactionIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.TransactionType;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.model.WalletTransaction;
import se.vegas.tasknz.repository.TransactionRepository;
import se.vegas.tasknz.repository.WalletRepository;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

/**
 * Date: 10.01.2024
 *
 * @author Nikolay Zinin
 */
@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final WalletService walletService;


    @Transactional
    public void makeTransaction(TransactionDTO transactionDTO, TransactionType transactionType) throws TransactionIdRedundantException, WalletNotFoundException, NotEnoughCreditException {
        //check if transactionId is unique
        Optional<WalletTransaction> transactionById = transactionRepository.findById(transactionDTO.getTransactionId());
        if (transactionById.isPresent()) {
            log.error(String.format("Transaction with id %s already exists", transactionDTO.getTransactionId()));
            throw new TransactionIdRedundantException("Transaction with id " +  transactionDTO.getTransactionId() +" already exists");
        }

        //credit or debit the balance
        Wallet wallet = walletService.getWalletByPlayerId(transactionDTO.getPlayerId());
        BigDecimal newBalance = wallet.getBalance();
        if (transactionType == TransactionType.CREDIT){
            newBalance =  newBalance.add(transactionDTO.getAmount());
        }
        else if (transactionType == TransactionType.DEBIT){
           newBalance = newBalance.subtract(transactionDTO.getAmount());
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                log.error("Not enough money on balance");
                throw new NotEnoughCreditException("Not enough money on balance");
            }
        }

        //persist the transaction and wallet
        WalletTransaction transaction = new MappingService().mapPaymentDtoToEntity(transactionDTO);
        wallet.setBalance(newBalance);
        transaction.setWallet(wallet);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionTime(Instant.now());
        walletRepository.save(wallet);
        transactionRepository.save(transaction);
    }
}
