package com.midnight.midnightledger.model;

import com.midnight.midnightledger.model.enums.RecurrenceType;
import com.midnight.midnightledger.model.enums.TransactionType;
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
@Table(name = "recurring_transaction")
public class RecurringTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_name", nullable = false)
    private String transactionName;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;  // e.g., INCOME, EXPENSES

    @Column(nullable = false)
    private String category;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "next_execution_date")
    private LocalDate nextExecutionDate;

    @Column(nullable = false)
    private boolean active = true; // Default to active
}
