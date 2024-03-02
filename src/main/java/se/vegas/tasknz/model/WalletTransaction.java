package se.vegas.tasknz.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Date: 10.01.2024
 *
 * @author Nikolay Zinin
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="transaction")
public class WalletTransaction {

    @Id
    @Column
    private String id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType transactionType;
    @Column
    private BigDecimal amount;
    @ManyToOne(targetEntity = Wallet.class)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
    private Wallet wallet;
    @Column(name = "time")
    private Instant transactionTime;
}

