package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.dto.ProductRequest;
import com.kokos.onlineshop.domain.dto.ProductResponse;
import com.kokos.onlineshop.domain.dto.SearchRequest;
import com.kokos.onlineshop.domain.entity.Category;
import com.kokos.onlineshop.domain.entity.Product;
import com.kokos.onlineshop.exception.AlreadyExistsException;
import com.kokos.onlineshop.repository.ProductRepository;
import com.kokos.onlineshop.service.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByTitleAndBrand(request.title(), request.brand()))
            throw new AlreadyExistsException("Product already exists with title: " + request.title());
        Category category = categoryService.getCategoryById(request.categoryId());
        Product toSave = productMapper.toProduct(request, category);
        return productMapper.toProductResponse(productRepository.save(toSave));
    }

    public List<ProductResponse> getProducts(SearchRequest searchRequest) {
        Specification<Product> spec = Specification
                .where(ProductSpecification.hasCategoryEqual(searchRequest.categoryTitle()))
                .and(ProductSpecification.hasBrandEqual(searchRequest.brand()))
                .and(ProductSpecification.hasTitleLike(searchRequest.title()));
        return productRepository.findAll(spec).stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    public ProductResponse getProductResponseById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }
    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }


    @Transactional
    public ProductResponse updateProduct(ProductRequest productRequest) {
        Product forUpdate = productRepository.findById(productRequest.id())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productRequest.id()));
        Category categoryForUpdatedProduct = categoryService.getCategoryById(productRequest.categoryId());
        if (productRepository.existsByTitleAndIdNot(productRequest.title(), forUpdate.getId()));
        forUpdate.setTitle(productRequest.title());
        forUpdate.setBrand(productRequest.brand());
        forUpdate.setCategory(categoryForUpdatedProduct);
        forUpdate.setInventory(productRequest.inventory());
        forUpdate.setPrice(productRequest.price());
        forUpdate.setDescription(productRequest.description());
        return productMapper.toProductResponse(forUpdate);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
