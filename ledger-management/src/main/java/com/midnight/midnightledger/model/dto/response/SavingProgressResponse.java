package com.midnight.midnightledger.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavingProgressResponse {

    private String startMonth;

    private String startYear;

    private String latestMonth;

    private String latestYear;

    private BigDecimal totalGoalAmount;

    private BigDecimal currentSaving;
}
