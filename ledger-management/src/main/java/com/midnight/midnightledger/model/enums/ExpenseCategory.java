package com.midnight.midnightledger.model.enums;

import com.midnight.midnightledger.model.dto.response.MonthlyTransactionResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ExpenseCategory {
    HOUSING("Housing"),
    TRANSPORTATION("Transportation"),
    FOOD("Food"),
    UTILITIES("Utilities"),
    INSURANCE("Insurance"),
    HEALTHCARE("Healthcare"),
    ENTERTAINMENT("Entertainment"),
    CLOTHING("Clothing"),
    EDUCATION("Education"),
    PERSONAL_CARE("Personal Care"),
    MISCELLANEOUS("Miscellaneous");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public static Map<String, BigDecimal> getExpenseMap() {
        Map<String, BigDecimal> expenseMap = new HashMap<>();
        for (ExpenseCategory category : ExpenseCategory.values()) {
            expenseMap.put(category.displayName, new BigDecimal(0));
        }
        return expenseMap;
    }

}
