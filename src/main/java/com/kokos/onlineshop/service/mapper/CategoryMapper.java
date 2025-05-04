package com.kokos.onlineshop.service.mapper;

import com.kokos.onlineshop.domain.dto.CategoryResponse;
import com.kokos.onlineshop.domain.entity.Category;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
    public CategoryResponse toCategoryResponse(Category category){
        return new CategoryResponse(
                category.getId(),
                category.getTitle(),
                category.getProducts().size());
    }
}
