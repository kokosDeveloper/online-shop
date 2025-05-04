package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.dto.CategoryRequest;
import com.kokos.onlineshop.domain.dto.CategoryResponse;
import com.kokos.onlineshop.domain.entity.Category;
import com.kokos.onlineshop.exception.AlreadyExistsException;
import com.kokos.onlineshop.repository.CategoryRepository;
import com.kokos.onlineshop.service.mapper.CategoryMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByTitle(request.title()))
            throw new AlreadyExistsException("Category already exists with title: " + request.title());
        Category category = Category.builder()
                .title(request.title())
                .build();
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getCategoryResponseById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryResponse)
                .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + id));
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public CategoryResponse updateCategory(CategoryRequest request) {
        Category forUpdate = categoryRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + request.id()));
        if(categoryRepository.existsByTitleAndIdNot(request.title(), request.id()))
            throw new AlreadyExistsException("Category already exists with title: " + request.title());
        forUpdate.setTitle(request.title());
        return categoryMapper.toCategoryResponse(forUpdate);
    }
    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No category found with id: " + id));
    }
}
