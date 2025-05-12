package com.kokos.onlineshop.controller;

import com.kokos.onlineshop.domain.dto.CategoryRequest;
import com.kokos.onlineshop.domain.dto.CategoryResponse;
import com.kokos.onlineshop.domain.dto.groups.OnUpdate;
import com.kokos.onlineshop.service.CategoryService;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping
    @PreAuthorize("hasAuthority('addCategory')")
    public ResponseEntity<CategoryResponse> createCategory(
            @Validated({Default.class}) @RequestBody CategoryRequest request
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(categoryService.getCategoryResponseById(id));
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('deleteCategory')")
    public ResponseEntity<Void> deleteCategoryById(
            @PathVariable Long id
    ){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    @PreAuthorize("hasAuthority('updateCategory')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @Validated({Default.class, OnUpdate.class}) @RequestBody CategoryRequest request
    ){
        return ResponseEntity.ok(categoryService.updateCategory(request));
    }
}
