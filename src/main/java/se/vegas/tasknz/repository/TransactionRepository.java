package se.vegas.tasknz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.vegas.tasknz.model.WalletTransaction;

/**
 * Date: 10.01.2024
 *
 * @author Nikolay Zinin
 */
public interface TransactionRepository extends JpaRepository<WalletTransaction, String> {
}
