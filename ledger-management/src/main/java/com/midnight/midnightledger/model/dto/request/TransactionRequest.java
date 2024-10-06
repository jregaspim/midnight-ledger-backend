package com.midnight.midnightledger.model.dto.request;

import com.midnight.midnightledger.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private Long id;

    private Long accountId;

    private BigDecimal amount;

    private TransactionType transactionType;

    private String category;

    private LocalDate transactionDate;

    private String description;


}
