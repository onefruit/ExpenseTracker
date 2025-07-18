package com.prabinsoft.expense.service;

import com.prabinsoft.expense.dto.income.IncomeRequest;
import com.prabinsoft.expense.dto.income.IncomeResponse;
import com.prabinsoft.expense.entity.Category;
import com.prabinsoft.expense.entity.Income;
import com.prabinsoft.expense.entity.Profile;
import com.prabinsoft.expense.repo.CategoryRepo;
import com.prabinsoft.expense.repo.IncomeRepo;
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
public class IncomeService {
    private final IncomeRepo incomeRepo;
    private final CategoryRepo categoryRepo;
    private final ProfileService profileService;
    private final ModelMapper modelMapper;

    public IncomeResponse addResponse(IncomeRequest request) {
        // Get current profile
        Profile currentProfile = profileService.getCurrentProfile();

        // Get category or throw
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // If updating existing income
        Income income;
        if (request.getId() != null) {
            income = incomeRepo.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Income not found"));
            modelMapper.map(request, income); // update existing
        } else {
            income = modelMapper.map(request, Income.class); // new entry
        }

        // Set profile and category (ModelMapper won't map these)
        income.setProfile(currentProfile);
        income.setCategory(category);

        // Save and return
        Income saved = incomeRepo.save(income);
        return modelMapper.map(saved, IncomeResponse.class);
    }

    public List<IncomeResponse> getCurrentMonthExpensesForCurrentUser() {
        Profile currentProfile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<Income> incomeList = incomeRepo.findByProfileIdAndDateBetween(currentProfile.getId(), startDate, endDate);
        return incomeList.stream().map(e -> modelMapper.map(e, IncomeResponse.class)).collect(Collectors.toList());
    }

    public boolean deleteIncome(Integer incomeId) {
        Profile currentProfile = profileService.getCurrentProfile();
        Income income = incomeRepo.findById(incomeId).orElseThrow(() -> new RuntimeException("Income not found"));
        if (!income.getProfile().getId().equals(currentProfile.getId())) {
            throw new RuntimeException("Unauthorized to delete this income");
        }
        incomeRepo.delete(income);
        return true;
    }

    public List<IncomeResponse> getLatest5IncomeForCurrentUser() {
        Profile currentProfile = profileService.getCurrentProfile();
        List<Income> list = incomeRepo.findTop5ByProfileIdOrderByDateDesc(currentProfile.getId());
        return list.stream().map(e -> modelMapper.map(e, IncomeResponse.class)).collect(Collectors.toList());
    }

    public BigDecimal getTotalIncomeForCurrentUser() {
        Profile currentProfile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepo.findTotalExpenseByProfileId(currentProfile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

}
