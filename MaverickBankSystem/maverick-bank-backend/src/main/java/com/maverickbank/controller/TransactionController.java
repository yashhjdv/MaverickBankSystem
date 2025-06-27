package com.maverickbank.controller;

import com.maverickbank.entity.Transaction;
import com.maverickbank.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;


    @Operation(summary = "Get transactions by account ID", description = "Returns all transactions for a specific account.")
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(
            @Parameter(name = "accountId", description = "Account ID", required = true, example = "1")
            @PathVariable(name = "accountId") Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.deposit(request.getAccountId(), request.getAmount()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(request.getAccountId(), request.getAmount()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount()));
    }

    public static class TransactionRequest {
        private Long accountId;
        private java.math.BigDecimal amount;
        public Long getAccountId() { return accountId; }
        public void setAccountId(Long accountId) { this.accountId = accountId; }
        public java.math.BigDecimal getAmount() { return amount; }
        public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
    }
    public static class TransferRequest {
        private Long fromAccountId;
        private Long toAccountId;
        private java.math.BigDecimal amount;
        public Long getFromAccountId() { return fromAccountId; }
        public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }
        public Long getToAccountId() { return toAccountId; }
        public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }
        public java.math.BigDecimal getAmount() { return amount; }
        public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
    }
}
