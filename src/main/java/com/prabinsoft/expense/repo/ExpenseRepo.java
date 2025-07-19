package com.prabinsoft.expense.repo;

import com.prabinsoft.expense.entity.Expense;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepo extends JpaRepository<Expense, Integer> {

    List<Expense> findByProfileIdOrderByDateDesc(Integer profileId);

    List<Expense> findTop5ByProfileIdOrderByDateDesc(Integer profileId);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.profile.id = :profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Integer profileId);

    List<Expense> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase
            (Integer profileId, LocalDate startDate, LocalDate endDate, String keyword, Sort sort);

    List<Expense> findByProfileIdAndDateBetween(Integer profileId, LocalDate startDate, LocalDate endDate);

    List<Expense> findByProfileIdAndDate(Integer profileId, LocalDate date);

}
