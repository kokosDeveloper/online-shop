package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasTitleLike(String title){
        return (root, query, cb) -> {
            if (title == null)
                return cb.conjunction();
            return cb.like(root.get("title"), "%" + title + "%");
        };
    }

    public static Specification<Product> hasCategoryEqual(String category){
        return (root, query, cb) -> {
            if(category == null)
                return cb.conjunction();
            return cb.equal(root.get("category").get("title"), category);
        };
    }

    public static Specification<Product> hasBrandEqual(String brand){
        return (root, query, cb) -> {
            if(brand == null)
                return cb.conjunction();
            return cb.equal(root.get("brand"), brand);
        };
    }
}
