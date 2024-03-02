package se.vegas.tasknz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Date: 25.01.2024
 *
 * @author Nikolay Zinin
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletService walletService;
    private TransactionService transactionService;

    @BeforeEach
    public void initTransactionService() {
        transactionService = new TransactionService(transactionRepository, walletRepository, walletService);
    }
    @Test
    public void testCreditTransaction() throws WalletNotFoundException, TransactionIdRedundantException, NotEnoughCreditException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId("transactionId1")
                .playerId("playerId1")
                .amount(BigDecimal.valueOf(100.0))
                .build();

        Wallet wallet =  Wallet.builder()
                .playerId("playerId1")
                .balance(BigDecimal.valueOf(500.0))
                .build();

        Mockito.when(walletService.getWalletByPlayerId("playerId1")).thenReturn(wallet);
        Mockito.when(transactionRepository.findById("transactionId1")).thenReturn(Optional.empty());
       // transactionService.credit(transactionDTO);
        transactionService.makeTransaction(transactionDTO, TransactionType.CREDIT);
        Mockito.verify(walletRepository).save(wallet);
        Mockito.verify(transactionRepository).save(Mockito.any(WalletTransaction.class));
    }

    @Test
    public void testDebitTransaction() throws WalletNotFoundException, NotEnoughCreditException, TransactionIdRedundantException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId("transactionId1")
                .playerId("playerId1")
                .amount(BigDecimal.valueOf(100.0))
                .build();

        Wallet wallet =  Wallet.builder()
                .playerId("playerId1")
                .balance(BigDecimal.valueOf(500.0))
                .build();

        Mockito.when(walletService.getWalletByPlayerId("playerId1")).thenReturn(wallet);
        Mockito.when(transactionRepository.findById("transactionId1")).thenReturn(Optional.empty());
       // transactionService.debit(transactionDTO);
        transactionService.makeTransaction(transactionDTO, TransactionType.DEBIT);
        Mockito.verify(walletRepository).save(wallet);
        Mockito.verify(transactionRepository).save(Mockito.any(WalletTransaction.class));
    }

    @Test
    public void testCreditTransactionWithExistingTransaction() {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId("transactionId2")
                .build();

        Mockito.when(transactionRepository.findById("transactionId2")).thenReturn(Optional.of(new WalletTransaction()));
        assertThrows(TransactionIdRedundantException.class, () ->  transactionService.makeTransaction(transactionDTO, TransactionType.CREDIT));
    }

    @Test
    public void testDebitTransactionWithNotEnoughCredit() throws WalletNotFoundException {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .playerId("playerId1")
                .amount(BigDecimal.valueOf(600.00))
                .build();

        Wallet wallet =  Wallet.builder()
                .playerId("playerId1")
                .balance(BigDecimal.valueOf(500.00))
                .build();

        Mockito.when(walletService.getWalletByPlayerId("playerId1")).thenReturn(wallet);
        assertThrows(NotEnoughCreditException.class, () ->  transactionService.makeTransaction(transactionDTO, TransactionType.DEBIT));
    }
}