package com.jitendra.productservice.service;

import com.jitendra.productservice.Repository.ProductRepo;
import com.jitendra.productservice.dto.ProductRequest;
import com.jitendra.productservice.dto.ProductResponse;
import com.jitendra.productservice.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepo productRepo;

    public void createProduct(ProductRequest productRequest){
        Product product=Product.builder()
                .name(productRequest.getName()).description(productRequest.getDescription()).price(productRequest.getPrice())
                .build();
        productRepo.save(product);
        log.info("Product {} is saved",product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product>products=productRepo.findAll();
        return products.stream().map(product->mapToProductResponse(product)).toList();

    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
