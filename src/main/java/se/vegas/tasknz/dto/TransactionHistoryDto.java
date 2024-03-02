package se.vegas.tasknz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Date: 20.01.2024
 *
 * @author Nikolay Zinin
 */
@Data
@Builder
@AllArgsConstructor
public class TransactionHistoryDto {
    private String transactionType;
    private BigDecimal amount;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
    private Instant transactionTime;

    public TransactionHistoryDto() {

    }
}
