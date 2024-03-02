package se.vegas.tasknz.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import se.vegas.tasknz.dto.*;
import se.vegas.tasknz.exception.NotEnoughCreditException;
import se.vegas.tasknz.exception.PlayerIdRedundantException;
import se.vegas.tasknz.exception.TransactionIdRedundantException;
import se.vegas.tasknz.exception.WalletNotFoundException;
import se.vegas.tasknz.model.TransactionType;
import se.vegas.tasknz.service.TransactionService;
import se.vegas.tasknz.service.WalletService;

import java.util.List;

/**
 * Date: 10.01.2024
 *
 * @author Nikolay Zinin
 */
@RestController
@RequestMapping("/api/wallet")
@AllArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final TransactionService transactionService;

    @PostMapping (path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createWallet(@RequestBody PlayerDto playerDto) throws PlayerIdRedundantException {
        walletService.createWallet(playerDto.getPlayerId());
    }
    @PostMapping (path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void deleteWallet(@RequestBody WalletDto walletDto) throws PlayerIdRedundantException {
        walletService.deleteWallet(walletDto.getWalletId());
    }

    @GetMapping(path = "/balance")
    public BalanceDto getPlayerCurrentBalance(@RequestParam String playerId) throws WalletNotFoundException {
        return walletService.getPlayerBalance(playerId);
    }

    @PostMapping(path = "/credit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addCreditToPlayerBalance(@RequestBody TransactionDTO transactionDTO)
            throws WalletNotFoundException, TransactionIdRedundantException, NotEnoughCreditException {
        transactionService.makeTransaction(transactionDTO, TransactionType.CREDIT);
    }

    @PostMapping(path = "/debit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void withdrawalFromPlayerBalance(@RequestBody TransactionDTO transactionDTO)
            throws WalletNotFoundException, NotEnoughCreditException, TransactionIdRedundantException {
        transactionService.makeTransaction(transactionDTO, TransactionType.DEBIT);
    }

    @GetMapping ("/history")
    public List<TransactionHistoryDto> getPlayerTransactionHistory(@RequestParam String playerId)
            throws WalletNotFoundException {
        return walletService.getAllTransactionsByPlayerId(playerId);
    }
}
