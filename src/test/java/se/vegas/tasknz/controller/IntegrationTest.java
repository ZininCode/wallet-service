package se.vegas.tasknz.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import se.vegas.tasknz.dto.BalanceDto;
import se.vegas.tasknz.dto.PlayerDto;
import se.vegas.tasknz.dto.TransactionDTO;
import se.vegas.tasknz.model.TransactionType;
import se.vegas.tasknz.service.TransactionService;
import se.vegas.tasknz.service.WalletService;

import java.math.BigDecimal;


/**
 * Date: 25.01.2024
 *
 * @author Nikolay Zinin
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(WalletController.class)
@AutoConfigureMockMvc
class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WalletService walletService;
    @MockBean
    private TransactionService transactionService;
    private TransactionDTO transactionDTO;
    private String transactionJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        transactionDTO = TransactionDTO.builder()
                .transactionId("123456")
                .playerId("playerId1")
                .amount(BigDecimal.valueOf(100.0))
                .build();
        transactionJson = objectMapper.writeValueAsString(transactionDTO);
    }

    @Test
    public void testAddCreditToPlayerBalance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(transactionService).makeTransaction(any(TransactionDTO.class), any (TransactionType.class));
    }

    @Test
    public void testDebitToPlayerBalance() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet/debit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(transactionService).makeTransaction(any(TransactionDTO.class), any (TransactionType.class));
    }

    @Test
    void createWalletTest() throws Exception {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId("playerId1");
        JSONObject json = new JSONObject();
        json.put("playerId", "playerId1");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallet/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(json)))
                .andExpect(status().isOk());
        verify(walletService).createWallet(playerDto.getPlayerId());
    }

    @Test
    void getPlayerCurrentBalanceTest() throws Exception {
        String playerId = "playerId1";
        BalanceDto balanceDto = BalanceDto.builder()
                .playerId("playerId1")
                .balance(BigDecimal.valueOf(100))
                .build();
        when(walletService.getPlayerBalance(playerId)).thenReturn(balanceDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/wallet/balance")
                        .param("playerId", playerId))
                .andExpect(status().isOk());
        verify(walletService).getPlayerBalance(playerId);
    }
}