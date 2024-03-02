package se.vegas.tasknz.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import se.vegas.tasknz.dto.BalanceDto;
import se.vegas.tasknz.dto.TransactionHistoryDto;
import se.vegas.tasknz.exception.PlayerIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.model.WalletTransaction;
import se.vegas.tasknz.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Date: 25.01.2024
 *
 * @author Nikolay Zinin
 */
@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private MappingService mappingService;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void getWalletByPlayerId() throws WalletNotFoundException {
        String playerId = "playerId1";
        Wallet wallet = Wallet.builder()
                .playerId(playerId)
                .balance(BigDecimal.TEN)
                .build();
        when(walletRepository.findByPlayerId(playerId)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.getWalletByPlayerId(playerId);

        assertNotNull(result);
        assertEquals(playerId, result.getPlayerId());
        assertEquals(BigDecimal.TEN, result.getBalance());
    }

    @Test
    void getWalletByPlayerId_NotFound() {
        String playerId = "playerId2";
        when(walletRepository.findByPlayerId(playerId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> walletService.getWalletByPlayerId(playerId));
    }

    @Test
    void getPlayerBalance_ShouldMapWalletToBalanceDto() throws WalletNotFoundException {
        String playerId = "playerId3";
        Wallet wallet = Wallet.builder()
                .playerId(playerId)
                .balance(BigDecimal.valueOf(20))
                .build();
        when(walletRepository.findByPlayerId(playerId)).thenReturn(Optional.of(wallet));
        when(mappingService.mapWalletToBalanceDto(wallet)).thenReturn(new BalanceDto(playerId, BigDecimal.valueOf(20)));

        BalanceDto result = walletService.getPlayerBalance(playerId);

        assertNotNull(result);
        assertEquals(playerId, result.getPlayerId());
        assertEquals(BigDecimal.valueOf(20), result.getBalance());
    }

    @Test
    void getAllTransactionsByPlayerId_ShouldMapTransactionsToDtoList() throws WalletNotFoundException {
        String playerId = "playerId4";
        Wallet wallet = Wallet.builder()
                .playerId(playerId)
                .balance(BigDecimal.valueOf(30))
                .walletTransactions(new ArrayList<>())
                .build();
        when(walletRepository.findByPlayerId(playerId)).thenReturn(Optional.of(wallet));

        List<TransactionHistoryDto> result = walletService.getAllTransactionsByPlayerId(playerId);

        assertNotNull(result);
        assertEquals(0, result.size()); // Assuming wallet transactions are empty in this test
    }

    @Test
    void createWallet_WhenPlayerIdIsNotRedundant_ShouldSaveWallet() throws PlayerIdRedundantException {
        String playerId = "playerId5";
        when(walletRepository.findByPlayerId(playerId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> walletService.createWallet(playerId));

        verify(walletRepository).save(Mockito.any());
    }

    @Test
    void createWallet_WhenPlayerIdIsRedundant_ShouldThrowException() {
        String playerId = "playerId6";
        when(walletRepository.findByPlayerId(playerId)).thenReturn(Optional.of(new Wallet()));

        assertThrows(PlayerIdRedundantException.class, () -> walletService.createWallet(playerId));
    }
}