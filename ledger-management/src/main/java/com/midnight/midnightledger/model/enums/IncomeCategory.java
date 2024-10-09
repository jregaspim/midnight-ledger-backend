package com.midnight.midnightledger.model.enums;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public enum IncomeCategory {
    SALARY("Salary"),
    BUSINESS("Business");

    private final String displayName;

    IncomeCategory(String displayName) {
        this.displayName = displayName;
    }

    public static Map<String, BigDecimal> getIncomeMap() {
        Map<String, BigDecimal> incomeMap = new HashMap<>();
        for (IncomeCategory category : IncomeCategory.values()) {
            incomeMap.put(category.displayName, new BigDecimal(0));
        }
        return incomeMap;
    }

}
