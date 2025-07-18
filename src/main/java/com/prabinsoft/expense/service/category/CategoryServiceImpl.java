package com.prabinsoft.expense.service.category;

import com.prabinsoft.expense.dto.category.CategoryRequest;
import com.prabinsoft.expense.dto.category.CategoryResponse;
import com.prabinsoft.expense.entity.Category;
import com.prabinsoft.expense.entity.Profile;
import com.prabinsoft.expense.repo.CategoryRepo;
import com.prabinsoft.expense.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl {
    private final ProfileService profileService;
    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;


    public CategoryResponse saveCategory(CategoryRequest request) {
        Category category = new Category();
        if (request.getId() != null) {
            categoryRepo.findById(request.getId()).orElse(category);
        }
        Profile currentProfile = profileService.getCurrentProfile();
        if (categoryRepo.existsByNameAndProfileId(request.getName(), currentProfile.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with this name already exists");
        }
        Category newCategory = modelMapper.map(request, Category.class);
        newCategory = categoryRepo.save(newCategory);
        return modelMapper.map(newCategory, CategoryResponse.class);
    }

    public List<CategoryResponse> getCategoriesForCurrentUser() {
        Profile currentProfile = profileService.getCurrentProfile();
        List<Category> categories = categoryRepo.findByProfileId(currentProfile.getId());
        return categories.stream().map(e -> modelMapper.map(e, CategoryResponse.class)).collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategoriesByTypeForCurrentUser(String type) {
        Profile currentProfile = profileService.getCurrentProfile();
        List<Category> categories = categoryRepo.findByTypeAndProfileId(type, currentProfile.getId());
        return categories.stream().map(e -> modelMapper.map(e, CategoryResponse.class)).collect(Collectors.toList());
    }

}
