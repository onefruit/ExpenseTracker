package com.prabinsoft.expense.controller;


import com.prabinsoft.expense.constants.ModuleNameConstants;
import com.prabinsoft.expense.dto.expense.ExpenseRequest;
import com.prabinsoft.expense.dto.expense.ExpenseResponse;
import com.prabinsoft.expense.enums.Message;
import com.prabinsoft.expense.exception.GlobalApiResponse;
import com.prabinsoft.expense.service.expense.ExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
@Tag(name = ModuleNameConstants.INCOME)
public class ExpenseController extends BaseController {

    private final ExpenseService service;

    @PostConstruct
    public void init() {
        this.module = ModuleNameConstants.INCOME;
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalApiResponse> create(@Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = service.addResponse(request);
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), response));
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalApiResponse> get() {
        List<ExpenseResponse> currentMonthExpensesForCurrentUser = service.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), currentMonthExpensesForCurrentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalApiResponse> delete(@PathVariable Integer id) {
        boolean result = service.deleteExpense(id);
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), result));
    }

    @GetMapping("/latest")
    public ResponseEntity<GlobalApiResponse> getLatest5IncomeForCurrentUser() {
        List<ExpenseResponse> expense = service.getLatest5ExpenseForCurrentUser();
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.getMessage(Message.SAVE.getCode(), module),
                        expense
                )
        );
    }

    @GetMapping("/total")
    public ResponseEntity<GlobalApiResponse> getTotalIncomeForCurrentUser() {
        BigDecimal totalIncome = service.getTotalExpenseForCurrentUser();
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.getMessage(Message.SAVE.getCode(), module),
                        totalIncome
                )
        );
    }
}
