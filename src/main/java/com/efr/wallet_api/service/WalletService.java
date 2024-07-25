package com.efr.wallet_api.service;

import com.efr.wallet_api.dao.WalletRepository;
import com.efr.wallet_api.dao.WalletTransactionRepository;
import com.efr.wallet_api.dto.OperationType;
import com.efr.wallet_api.dto.WalletTransactionDTORequest;
import com.efr.wallet_api.entity.Wallet;
import com.efr.wallet_api.entity.WalletTransaction;
import com.efr.wallet_api.exception.InsufficientFundsException;
import com.efr.wallet_api.exception.WalletIsInDatabaseException;
import com.efr.wallet_api.exception.WalletNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    public BigDecimal getBalance(UUID walletId) throws WalletNotFoundException {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);
        return wallet.getBalance();
    }

    @Transactional
    public void transaction(WalletTransactionDTORequest request) throws WalletNotFoundException, InsufficientFundsException {
        Wallet wallet = walletRepository.findById(request.getId()).orElseThrow(WalletNotFoundException::new);

        BigDecimal newBalance;
        if (request.getOperationType() == OperationType.DEPOSIT) {
            newBalance = wallet.getBalance().add(request.getAmount());
        } else {
            if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                throw new InsufficientFundsException();
            }
            newBalance = wallet.getBalance().subtract(request.getAmount());
        }

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setId(UUID.randomUUID());
        walletTransaction.setWalletId(wallet.getId());
        walletTransaction.setOperationType(request.getOperationType().name());
        walletTransaction.setAmount(request.getAmount());
        walletTransaction.setCreatedAt(LocalDateTime.now());
        walletTransactionRepository.save(walletTransaction);
    }

    public UUID newRandomWallet() throws WalletIsInDatabaseException {
        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
            if (walletRepository.findById(wallet.getId()).isPresent()){
                throw new WalletIsInDatabaseException();
            }
            wallet.setBalance(BigDecimal.valueOf(new Random().nextDouble() * 100000));
            walletRepository.save(wallet);
            return wallet.getId();
    }
}
