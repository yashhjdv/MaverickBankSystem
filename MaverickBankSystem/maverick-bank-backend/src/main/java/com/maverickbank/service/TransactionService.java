package com.maverickbank.service;

import com.maverickbank.entity.Transaction;
import com.maverickbank.entity.Account;
import com.maverickbank.repository.TransactionRepository;
import com.maverickbank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Transaction deposit(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        Transaction tx = new Transaction();
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setDate(LocalDateTime.now());
        tx.setAccount(account);
        tx.setDescription("Deposit");
        return transactionRepository.save(tx);
    }

    public Transaction withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        if (account.getBalance().compareTo(amount) < 0) throw new RuntimeException("Insufficient funds");
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        Transaction tx = new Transaction();
        tx.setType("WITHDRAWAL");
        tx.setAmount(amount);
        tx.setDate(LocalDateTime.now());
        tx.setAccount(account);
        tx.setDescription("Withdrawal");
        return transactionRepository.save(tx);
    }

    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        withdraw(fromAccountId, amount);
        deposit(toAccountId, amount);
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow();
        Transaction tx = new Transaction();
        tx.setType("TRANSFER");
        tx.setAmount(amount);
        tx.setDate(LocalDateTime.now());
        tx.setAccount(fromAccount);
        tx.setDescription("Transfer to account " + toAccountId);
        return transactionRepository.save(tx);
    }
}
