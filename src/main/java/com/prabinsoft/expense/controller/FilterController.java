package com.prabinsoft.expense.controller;

import com.prabinsoft.expense.dto.FilterDTO;
import com.prabinsoft.expense.dto.expense.ExpenseResponse;
import com.prabinsoft.expense.dto.income.IncomeResponse;
import com.prabinsoft.expense.service.IncomeService;
import com.prabinsoft.expense.service.expense.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {
    private final ExpenseService expenseService;
    private final IncomeService incomeService;


    @PostMapping
    public ResponseEntity<?> filterTransaction(@RequestBody FilterDTO filter) {
        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
        String keyword = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        if ("income".equalsIgnoreCase(filter.getType())) {
            List<IncomeResponse> incomeList = incomeService.filterIncome(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(incomeList);
        } else if ("expense".equalsIgnoreCase(filter.getType())) {
            List<ExpenseResponse> expenseResponses = expenseService.filterExpense(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(expenseResponses);
        } else {
            return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expense' ");
        }

    }
}
