package com.prabinsoft.expense.controller;


import com.prabinsoft.expense.constants.ModuleNameConstants;
import com.prabinsoft.expense.dto.income.IncomeRequest;
import com.prabinsoft.expense.dto.income.IncomeResponse;
import com.prabinsoft.expense.enums.Message;
import com.prabinsoft.expense.exception.GlobalApiResponse;
import com.prabinsoft.expense.service.IncomeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/income")
@RequiredArgsConstructor
@Tag(name = ModuleNameConstants.INCOME)
public class IncomeController extends BaseController {

    private final IncomeService service;

    @PostConstruct
    public void init() {
        this.module = ModuleNameConstants.INCOME;
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalApiResponse> create(@Valid @RequestBody IncomeRequest request) {
        IncomeResponse response = service.addResponse(request);
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), response));
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalApiResponse> get() {
        List<IncomeResponse> currentMonthExpensesForCurrentUser = service.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), currentMonthExpensesForCurrentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalApiResponse> delete(@PathVariable Integer id) {
        boolean result = service.deleteIncome(id);
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), result));
    }

    @GetMapping("/latest")
    public ResponseEntity<GlobalApiResponse> getLatest5IncomeForCurrentUser() {
        List<IncomeResponse> latestIncome = service.getLatest5IncomeForCurrentUser();
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.getMessage(Message.SAVE.getCode(), module),
                        latestIncome
                )
        );
    }

    @GetMapping("/total")
    public ResponseEntity<GlobalApiResponse> getTotalIncomeForCurrentUser() {
        BigDecimal totalIncome = service.getTotalIncomeForCurrentUser();
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.getMessage(Message.SAVE.getCode(), module),
                        totalIncome
                )
        );
    }

}
