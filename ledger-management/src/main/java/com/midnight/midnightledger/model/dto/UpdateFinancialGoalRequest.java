package com.midnight.midnightledger.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateFinancialGoalRequest {
    private BigDecimal currentAmount;
}