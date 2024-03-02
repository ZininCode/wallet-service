package se.vegas.tasknz.model;


import lombok.*;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;

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
@Table(name ="wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column (name = "player_id")
    private String playerId;
    @Column
    private BigDecimal balance;
    @OneToMany(mappedBy = "wallet", targetEntity = WalletTransaction.class)
    private List<WalletTransaction> walletTransactions;
}
