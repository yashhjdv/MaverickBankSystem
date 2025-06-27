package com.maverickbank.controller;


import com.maverickbank.entity.Loan;
import com.maverickbank.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;


    @Operation(summary = "Get all loans", description = "Returns a list of all loans.")
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }


    @Operation(summary = "Get loan by ID", description = "Returns a loan by its ID.")
    @GetMapping("/{loanId}")
    public ResponseEntity<Loan> getLoanById(
            @Parameter(name = "loanId", description = "Loan ID", required = true, example = "1")
            @PathVariable(name = "loanId") Long loanId) {
        return ResponseEntity.ok(loanService.getLoanById(loanId));
    }


    @Operation(summary = "Get loans by user ID", description = "Returns all loans for a specific user.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getLoansByUser(
            @Parameter(name = "userId", description = "User ID", required = true, example = "1")
            @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(loanService.getLoansByUserId(userId));
    }

    @Operation(summary = "Apply for a loan", description = "Apply for a new loan.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = LoanApplyRequest.class),
            examples = {@io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\n  \"userId\": 1,\n  \"amount\": 50000.00,\n  \"interestRate\": 8.5,\n  \"tenureMonths\": 36\n}")
            })
        )
    )
    @PostMapping
    public ResponseEntity<Loan> applyLoan(@Valid @org.springframework.web.bind.annotation.RequestBody LoanApplyRequest request) {
        return ResponseEntity.ok(loanService.applyLoan(
            request.getUserId(),
            request.getAmount(),
            request.getInterestRate(),
            request.getTenureMonths()
        ));
    }


    @Operation(summary = "Update loan status", description = "Update the status of a loan.",
        requestBody = @RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = LoanStatusUpdateRequest.class),
            examples = {@io.swagger.v3.oas.annotations.media.ExampleObject(
                value = "{\n  \"status\": \"APPROVED\"\n}")
            })
        )
    )
    @PutMapping("/{loanId}/status")
    public ResponseEntity<Loan> updateLoanStatus(
            @Parameter(name = "loanId", description = "Loan ID", required = true, example = "1")
            @PathVariable(name = "loanId") Long loanId,
            @Valid @org.springframework.web.bind.annotation.RequestBody LoanStatusUpdateRequest request) {
        return ResponseEntity.ok(loanService.updateLoanStatus(loanId, request.getStatus()));
    }

    public enum LoanStatus {
        APPLIED,
        APPROVED,
        REJECTED,
        DISBURSED
    }

    @Schema(description = "Request body for applying for a loan")
    public static class LoanApplyRequest {
        @Schema(description = "User ID", example = "1", required = true)
        @NotNull(message = "User ID is required")
        private Long userId;

        @Schema(description = "Loan amount", example = "50000.00", required = true)
        @NotNull(message = "Loan amount is required")
        @DecimalMin(value = "1000.00", message = "Loan amount must be at least 1000.00")
        private BigDecimal amount;

        @Schema(description = "Interest rate", example = "8.5", required = true)
        @DecimalMin(value = "1.0", message = "Interest rate must be at least 1.0%")
        @DecimalMax(value = "30.0", message = "Interest rate cannot exceed 30.0%")
        private double interestRate;

        @Schema(description = "Loan tenure in months", example = "36", required = true)
        @Min(value = 6, message = "Minimum loan tenure is 6 months")
        @Max(value = 360, message = "Maximum loan tenure is 360 months (30 years)")
        private int tenureMonths;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public double getInterestRate() { return interestRate; }
        public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
        public int getTenureMonths() { return tenureMonths; }
        public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
    }

    @Schema(description = "Request body for updating loan status")
    public static class LoanStatusUpdateRequest {
        @Schema(description = "Loan status", example = "APPROVED", required = true, allowableValues = {"APPLIED", "APPROVED", "REJECTED", "DISBURSED"})
        @NotNull(message = "Loan status is required")
        private LoanStatus status;
        public LoanStatus getStatus() { return status; }
        public void setStatus(LoanStatus status) { this.status = status; }
    }
}
