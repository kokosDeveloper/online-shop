package com.kokos.onlineshop.service.mapper;

import com.kokos.onlineshop.domain.dto.ProductRequest;
import com.kokos.onlineshop.domain.dto.ProductResponse;
import com.kokos.onlineshop.domain.entity.Category;
import com.kokos.onlineshop.domain.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public ProductResponse toProductResponse(Product product){
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getBrand(),
                product.getDescription(),
                product.getPrice(),
                product.getInventory(),
                product.getCategory().getId(),
                product.getImage()
        );
    }
    public Product toProduct(ProductRequest productRequest, Category category){
        return Product.builder()
                .title(productRequest.title())
                .brand(productRequest.brand())
                .description(productRequest.description())
                .price(productRequest.price())
                .inventory(productRequest.inventory())
                .category(category)
                .build();
    }
}
