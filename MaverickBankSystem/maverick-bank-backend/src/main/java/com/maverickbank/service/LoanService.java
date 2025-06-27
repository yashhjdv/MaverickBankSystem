package com.maverickbank.service;

import com.maverickbank.controller.LoanController.LoanStatus;
import com.maverickbank.entity.Loan;
import com.maverickbank.entity.User;
import com.maverickbank.repository.LoanRepository;
import com.maverickbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long loanId) {
        return loanRepository.findById(loanId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found"));
    }

    public List<Loan> getLoansByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return loanRepository.findByUserId(userId);
    }

    public Loan applyLoan(Long userId, BigDecimal amount, double interestRate, int tenureMonths) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Loan loan = new Loan();
        loan.setAmount(amount);
        loan.setInterestRate(interestRate);
        loan.setTenureMonths(tenureMonths);
        loan.setStatus(LoanStatus.APPLIED.name());
        loan.setApplicationDate(LocalDate.now());
        loan.setUser(user);
        return loanRepository.save(loan);
    }

    public Loan updateLoanStatus(Long loanId, LoanStatus newStatus) {
        Loan loan = loanRepository.findById(loanId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found"));

        // Only allow valid status transitions
        LoanStatus currentStatus = LoanStatus.valueOf(loan.getStatus());
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        loan.setStatus(newStatus.name());
        return loanRepository.save(loan);
    }

    private boolean isValidStatusTransition(LoanStatus current, LoanStatus next) {
        switch (current) {
            case APPLIED:
                return next == LoanStatus.APPROVED || next == LoanStatus.REJECTED;
            case APPROVED:
                return next == LoanStatus.DISBURSED;
            case REJECTED:
            case DISBURSED:
                return false;
            default:
                return false;
        }
    }
}
