package com.kokos.onlineshop.controller;

import com.kokos.onlineshop.domain.dto.ProductRequest;
import com.kokos.onlineshop.domain.dto.ProductResponse;
import com.kokos.onlineshop.domain.dto.SearchRequest;
import com.kokos.onlineshop.domain.dto.groups.OnUpdate;
import com.kokos.onlineshop.service.ProductService;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Validated(Default.class) @RequestBody ProductRequest request
    ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String brand,
            @RequestParam(value = "category-title", required = false) String categoryTitle
    ){
        SearchRequest searchRequest = new SearchRequest(title, brand, categoryTitle);
        return ResponseEntity.ok(productService.getProducts(searchRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(productService.getProductResponseById(id));
    }

    @PutMapping
    public ResponseEntity<ProductResponse> updateProduct(
            @Validated({Default.class, OnUpdate.class}) @RequestBody ProductRequest productRequest
    ){
        return ResponseEntity.ok(productService.updateProduct(productRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
