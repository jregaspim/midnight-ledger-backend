package com.midnight.midnightledger.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "debts")
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    private BigDecimal amount;

    private BigDecimal interestRate;

    private LocalDate dueDate;

    private String category;

    private String lender;

    private String repaymentSchedule;

    private BigDecimal remainingBalance;

    private String description;

}
