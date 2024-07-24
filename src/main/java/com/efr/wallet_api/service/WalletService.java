package com.efr.wallet_api.service;

import com.efr.wallet_api.dao.WalletRepository;
import com.efr.wallet_api.dao.WalletTransactionRepository;
import com.efr.wallet_api.entity.Wallet;
import com.efr.wallet_api.exception.WalletNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}
