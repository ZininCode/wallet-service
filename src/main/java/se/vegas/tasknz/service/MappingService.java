package se.vegas.tasknz.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import se.vegas.tasknz.dto.BalanceDto;
import se.vegas.tasknz.dto.TransactionDTO;
import se.vegas.tasknz.dto.TransactionHistoryDto;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.model.WalletTransaction;

import java.math.BigDecimal;

/**
 * Date: 10.01.2024
 *
 * @author Nikolay Zinin
 */
@Service
@AllArgsConstructor
public class MappingService {
    public WalletTransaction mapPaymentDtoToEntity(TransactionDTO transactionDTO) {
        return WalletTransaction.builder()
                .id(transactionDTO.getTransactionId())
                .amount(transactionDTO.getAmount())
                .build();
    }

    public BalanceDto mapWalletToBalanceDto(Wallet wallet) {
        BigDecimal balance = wallet.getBalance();
        if (balance == null) {
            balance = BigDecimal.valueOf(0);
        }
        return BalanceDto.builder()
                .playerId(wallet.getPlayerId())
                .balance(balance)
                .build();
    }

    public TransactionHistoryDto transactionToDto(WalletTransaction transaction) {
        return TransactionHistoryDto.builder()
                .transactionType(transaction.getTransactionType().name())
                .amount(transaction.getAmount())
                .transactionTime(transaction.getTransactionTime())
                .build();
    }
}
