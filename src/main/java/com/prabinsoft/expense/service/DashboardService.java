package com.prabinsoft.expense.service;

import com.prabinsoft.expense.dto.RecentTransactionDTO;
import com.prabinsoft.expense.dto.expense.ExpenseResponse;
import com.prabinsoft.expense.dto.income.IncomeResponse;
import com.prabinsoft.expense.entity.Profile;
import com.prabinsoft.expense.service.expense.ExpenseService;
import com.prabinsoft.expense.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData() {
        Profile profile = profileService.getCurrentProfile();
        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<IncomeResponse> latestIncome = incomeService.getLatest5IncomeForCurrentUser();
        List<ExpenseResponse> latestExpense = expenseService.getLatest5ExpenseForCurrentUser();

        List<RecentTransactionDTO> transactions = Stream.concat(
                        latestIncome.stream().map(income -> RecentTransactionDTO.builder()
                                .id(income.getId())
                                .name(income.getName())
                                .amount(income.getAmount())
                                .icon(income.getIcon())
                                .date(income.getDate())
                                .profileId(profile.getId())
                                .createdAt(income.getCreatedAt())
                                .updatedAt(income.getUpdatedAt())
                                .type("income")
                                .build()),
                        latestExpense.stream().map(expense -> RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .icon(expense.getIcon())
                                .date(expense.getDate())
                                .profileId(profile.getId())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .type("expense")
                                .build())
                )
                .sorted((a, b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                })
                .collect(Collectors.toList());

        returnValue.put("totalBalance", incomeService.getTotalIncomeForCurrentUser()
                .subtract(expenseService.getTotalExpenseForCurrentUser()));

        returnValue.put("totalIncome", incomeService.getTotalIncomeForCurrentUser());
        returnValue.put("totalExpense", expenseService.getTotalExpenseForCurrentUser());

        returnValue.put("recent5Income", latestIncome);

        returnValue.put("recentTransactions", transactions);

        return returnValue;
    }
}
