package com.midnight.midnightledger.model;

import com.midnight.midnightledger.model.enums.RecurrenceType;
import com.midnight.midnightledger.model.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recurring_transaction")
public class RecurringTransaction{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionName;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;  // e.g., INCOME, EXPENSES

    @Column(nullable = false)
    private String category;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;

    private LocalDate startDate;

    private LocalDate nextExecutionDate;

    private boolean active;

}
