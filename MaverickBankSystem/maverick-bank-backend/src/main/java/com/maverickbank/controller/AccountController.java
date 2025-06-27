package com.maverickbank.controller;

import com.maverickbank.entity.Account;
import com.maverickbank.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;


    @Operation(summary = "Get accounts by user ID", description = "Returns all accounts for a specific user.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUser(
            @Parameter(name = "userId", description = "User ID", required = true, example = "1")
            @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @Operation(summary = "Get account by account number", description = "Returns an account by its account number.")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(
            @Parameter(name = "accountNumber", description = "Account Number", required = true, example = "1234567890")
            @PathVariable(name = "accountNumber") String accountNumber) {
        return accountService.getAccountByNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/open")
    public ResponseEntity<Account> openAccount(@RequestBody AccountOpenRequest request) {
        return ResponseEntity.ok(accountService.openAccount(
            request.getUserId(),
            request.getAccountType(),
            request.getIfscCode(),
            request.getBranchName(),
            request.getBranchAddress(),
            request.getInitialBalance()
        ));
    }

    public static class AccountOpenRequest {
        private Long userId;
        private String accountType;
        private String ifscCode;
        private String branchName;
        private String branchAddress;
        private java.math.BigDecimal initialBalance;
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getAccountType() { return accountType; }
        public void setAccountType(String accountType) { this.accountType = accountType; }
        public String getIfscCode() { return ifscCode; }
        public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
        public String getBranchName() { return branchName; }
        public void setBranchName(String branchName) { this.branchName = branchName; }
        public String getBranchAddress() { return branchAddress; }
        public void setBranchAddress(String branchAddress) { this.branchAddress = branchAddress; }
        public java.math.BigDecimal getInitialBalance() { return initialBalance; }
        public void setInitialBalance(java.math.BigDecimal initialBalance) { this.initialBalance = initialBalance; }
    }
}
