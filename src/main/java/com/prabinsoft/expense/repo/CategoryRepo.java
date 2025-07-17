package com.prabinsoft.expense.repo;

import com.prabinsoft.expense.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

    List<Category> findByProfileId(Integer profileId);

    Optional<Category> findByIdAndProfileId(Integer id, Integer profileId);

    List<Category> findByTypeAndProfileId(String type, Integer profileId);

    Boolean existsByNameAndProfileId(String name, Integer profileId);
}
