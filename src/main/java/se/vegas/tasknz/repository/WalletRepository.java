package se.vegas.tasknz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.vegas.tasknz.model.Wallet;

import java.util.Optional;

/**
 * Date: 10.01.2024
 *
 * @author Nikolay Zinin
 */
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByPlayerId(String playerId);
}
