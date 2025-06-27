package com.maverickbank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private double interestRate;

    @Column(nullable = false)
    private int tenureMonths;

    @Column(nullable = false)
    private String status; // APPLIED, APPROVED, REJECTED, DISBURSED

    @Column(nullable = false)
    private LocalDate applicationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Loan() {}

    public Loan(Long id, BigDecimal amount, double interestRate, int tenureMonths, String status, LocalDate applicationDate, User user) {
        this.id = id;
        this.amount = amount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.status = status;
        this.applicationDate = applicationDate;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
