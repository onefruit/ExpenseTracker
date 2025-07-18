package com.prabinsoft.expense.service.expense;

import com.prabinsoft.expense.dto.expense.ExpenseRequest;
import com.prabinsoft.expense.dto.expense.ExpenseResponse;
import com.prabinsoft.expense.entity.Category;
import com.prabinsoft.expense.entity.Expense;
import com.prabinsoft.expense.entity.Profile;
import com.prabinsoft.expense.repo.CategoryRepo;
import com.prabinsoft.expense.repo.ExpenseRepo;
import com.prabinsoft.expense.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepo expenseRepo;
    private final CategoryRepo categoryRepo;
    private final ProfileService profileService;
    private final ModelMapper modelMapper;

    public ExpenseResponse addResponse(ExpenseRequest request) {
        // Get current profile
        Profile currentProfile = profileService.getCurrentProfile();

        // Get category or throw
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // If updating existing income
        Expense expense;
        if (request.getId() != null) {
            expense = expenseRepo.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Income not found"));
            modelMapper.map(request, expense); // update existing
        } else {
            expense = modelMapper.map(request, Expense.class); // new entry
        }

        // Set profile and category (ModelMapper won't map these)
        expense.setProfile(currentProfile);
        expense.setCategory(category);

        // Save and return
        Expense saved = expenseRepo.save(expense);
        return modelMapper.map(saved, ExpenseResponse.class);
    }

    // retrieves all expenses for the current month / based on start and end date
    public List<ExpenseResponse> getCurrentMonthExpensesForCurrentUser() {
        Profile currentProfile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<Expense> expenseList = expenseRepo.findByProfileIdAndDateBetween(currentProfile.getId(), startDate, endDate);
        return expenseList.stream().map(e -> modelMapper.map(e, ExpenseResponse.class)).collect(Collectors.toList());
    }

    public boolean deleteExpense(Integer expenseId) {
        Profile currentProfile = profileService.getCurrentProfile();
        Expense expense = expenseRepo.findById(expenseId).orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!expense.getProfile().getId().equals(currentProfile.getId())) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }
        expenseRepo.delete(expense);
        return true;
    }


    public List<ExpenseResponse> getLatest5ExpenseForCurrentUser() {
        Profile currentProfile = profileService.getCurrentProfile();
        List<Expense> list = expenseRepo.findTop5ByProfileIdOrderByDateDesc(currentProfile.getId());
        return list.stream().map(e -> modelMapper.map(e, ExpenseResponse.class)).collect(Collectors.toList());
    }

    public BigDecimal getTotalExpenseForCurrentUser() {
        Profile currentProfile = profileService.getCurrentProfile();
        BigDecimal total = expenseRepo.findTotalExpenseByProfileId(currentProfile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }


}
