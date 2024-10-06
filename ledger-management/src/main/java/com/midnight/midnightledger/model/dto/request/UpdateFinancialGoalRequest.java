package com.midnight.midnightledger.model.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateFinancialGoalRequest {
    private BigDecimal currentAmount;
}