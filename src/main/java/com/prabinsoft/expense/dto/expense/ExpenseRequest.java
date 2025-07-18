package com.prabinsoft.expense.dto.expense;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequest {
    private Integer id;
    private String name;
    private String icon;
    private LocalDate date;
    private BigDecimal amount;

    private Integer categoryId;
    private Integer profileId;
}
