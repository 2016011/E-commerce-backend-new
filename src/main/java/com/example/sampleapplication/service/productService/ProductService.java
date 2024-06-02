package com.example.sampleapplication.service.productService;

import com.example.sampleapplication.dto.ProductDto;
import com.example.sampleapplication.modal.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();
    Long createProducts(ProductDto productDto);
    Product getProductByProductId (Long productId);
    String deleteProduct(Long productId, Long imageId);
    void updateSoledQuantity(Long productId, Long soledQuantity);
}
