package se.vegas.tasknz.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import se.vegas.tasknz.dto.TransactionDTO;
import se.vegas.tasknz.model.Wallet;
import se.vegas.tasknz.repository.WalletRepository;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * Date: 25.01.2024
 *
 * @author Nikolay Zinin
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")

public class AcceptanceTest {

    @LocalServerPort
    int randomServerPort;
    private RestTemplate restTemplate;
    private String url;

    @Autowired
    private WalletRepository walletRepository;
    private String creditTransactionId;
    private String debitTransactionId;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        Wallet walletCredit = walletRepository.findByPlayerId("playerId1")
                .orElseGet(() ->
                        Wallet.builder()
                                .playerId("playerId1")
                                .balance(new BigDecimal(0.00))
                                .build()
                );
        Wallet walletDebit = walletRepository.findByPlayerId("playerId2")
                .orElseGet(() ->
                        Wallet.builder()
                                .playerId("playerId2")
                                .balance(new BigDecimal(0.00))
                                .build()
                );
        walletCredit.setBalance(BigDecimal.valueOf(1000.00));
        walletDebit.setBalance(BigDecimal.valueOf(1000.00));
        walletRepository.save(walletCredit);
        walletRepository.save(walletDebit);
        creditTransactionId = String.valueOf(UUID.randomUUID());
        debitTransactionId = String.valueOf(UUID.randomUUID());
    }

    @Test
    void testCredit() throws Exception {
        url = "http://localhost:" + randomServerPort + "/api/wallet/credit";
        BigDecimal balanceBeforeTransaction = walletRepository.findByPlayerId("playerId1").orElseThrow()
                .getBalance();
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .transactionId(creditTransactionId)
                .playerId("playerId1")
                .amount(BigDecimal.valueOf(100.00))
                .build();

        ResponseEntity responseEntity = restTemplate.postForEntity(url, transactionDTO, Object.class);

        BigDecimal balanceAfterTransaction = walletRepository.findByPlayerId("playerId1").orElseThrow()
                .getBalance();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(balanceAfterTransaction, balanceBeforeTransaction.add(BigDecimal.valueOf(100.00)));
    }

    @Test
    void testDebit() throws Exception {
      url = "http://localhost:" + randomServerPort + "/api/wallet/debit";
      BigDecimal balanceBeforeTransaction = walletRepository.findByPlayerId("playerId2").orElseThrow()
                .getBalance();
      TransactionDTO transactionDTO = TransactionDTO.builder()
              .transactionId(debitTransactionId)
              .playerId("playerId2")
              .amount(BigDecimal.valueOf(100.00))
              .build();

      ResponseEntity responseEntity = restTemplate.postForEntity(url, transactionDTO, Object.class);

      BigDecimal balanceAfterTransaction = walletRepository.findByPlayerId("playerId2").orElseThrow()
              .getBalance();
      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
      assertEquals(balanceAfterTransaction.add(BigDecimal.valueOf(100.00)), balanceBeforeTransaction);
    }
}
