package com.efr.wallet_api.controllers;

import com.efr.wallet_api.dto.WalletTransactionDTORequest;
import com.efr.wallet_api.exception.InsufficientFundsException;
import com.efr.wallet_api.exception.WalletIsInDatabaseException;
import com.efr.wallet_api.exception.WalletNotFoundException;
import com.efr.wallet_api.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("{walletId}")
    public ResponseEntity<?> getBalance(@PathVariable UUID walletId){
        try {
            BigDecimal balance = walletService.getBalance(walletId);
            return ResponseEntity.ok(balance);
        } catch (WalletNotFoundException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("wallet not found");
        }
    }

    @PostMapping
    public ResponseEntity<?> transaction(@Valid @RequestBody WalletTransactionDTORequest request){
        try {
            walletService.transaction(request);
            return ResponseEntity.ok().body("Successful transaction");
        }  catch (InsufficientFundsException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds");
        } catch (WalletNotFoundException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("wallet not found");
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping
    public ResponseEntity<?> addRandomWalletAndBalance(){
        try {
            UUID walletId = walletService.newRandomWallet();
            return ResponseEntity.ok().body(walletId);
        } catch (WalletIsInDatabaseException exception){
            return ResponseEntity.status(HttpStatus.FOUND).body("The wallet is already in the Database");
        }
    }
}
