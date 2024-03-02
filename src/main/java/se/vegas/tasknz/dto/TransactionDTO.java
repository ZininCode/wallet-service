package se.vegas.tasknz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Date: 20.01.2024
 *
 * @author Nikolay Zinin
 */
@Getter
@Setter
@Builder
public class TransactionDTO {
    private String transactionId;
    private String playerId;
    private BigDecimal amount;
}
