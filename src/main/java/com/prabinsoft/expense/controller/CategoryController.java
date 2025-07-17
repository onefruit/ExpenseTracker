package com.prabinsoft.expense.controller;

import com.prabinsoft.expense.constants.ModuleNameConstants;
import com.prabinsoft.expense.dto.category.CategoryRequest;
import com.prabinsoft.expense.dto.category.CategoryResponse;
import com.prabinsoft.expense.enums.Message;
import com.prabinsoft.expense.exception.GlobalApiResponse;
import com.prabinsoft.expense.service.category.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Tag(name = ModuleNameConstants.CATEGORY)
public class CategoryController extends BaseController {

    private final CategoryServiceImpl service;

    @PostConstruct
    public void init() {
        this.module = ModuleNameConstants.CATEGORY;
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalApiResponse> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = service.saveCategory(request);
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), response));
    }

    @GetMapping("/me")
    public ResponseEntity<GlobalApiResponse> getCategoriesByProfileId() {
        List<CategoryResponse> categoriesForCurrentUser = service.getCategoriesForCurrentUser();
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), categoriesForCurrentUser));
    }

    @GetMapping("/{type}")
    public ResponseEntity<GlobalApiResponse> getCategoriesByTypeByProfileId(@PathVariable String type) {
        List<CategoryResponse> categoriesForCurrentUser = service.getCategoriesByTypeForCurrentUser(type);
        return ResponseEntity.ok(successResponse(customMessageSource.getMessage(Message.SAVE.getCode(), module), categoriesForCurrentUser));
    }
}
