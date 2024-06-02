package com.example.sampleapplication.controller;

import com.example.sampleapplication.dto.ProductDto;
import com.example.sampleapplication.modal.Product;
import com.example.sampleapplication.service.AuthenticationFilter;
import com.example.sampleapplication.service.productService.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private static final Logger logger = LogManager.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @CrossOrigin
    @GetMapping("/get")
    public List<Product> getAllProducts() {
        try {
            return productService.getProducts();
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @CrossOrigin
    @GetMapping("/get/{id}")
    public Product getProductById(@PathVariable("id") Long id) {
        return productService.getProductByProductId(id);
    }

    @CrossOrigin
    @PostMapping("/create/product")
    public Long createProducts(@RequestParam(value = "addImageFile", required = false) MultipartFile addImageFile,
                               @RequestParam(value = "productDto", required = false) String productDtoJson) {
        logger.info("string Json -> : " + productDtoJson);
        ProductDto productDto = convertJsonToProductDto(productDtoJson);
        productDto.setFile(addImageFile);
        return productService.createProducts(productDto);
    }

    @CrossOrigin
    @DeleteMapping ("/deleteProduct/{id}/{imageId}")
    public String deleteProduct (@PathVariable Long id, @PathVariable Long imageId){
        try {
            return productService.deleteProduct(id,imageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "An error occurred while trying to delete AProduct with ID " + id;
    }

    private ProductDto convertJsonToProductDto(String json) {
        try {
            logger.info("string Json -> : " + json);
            return objectMapper.readValue(json, ProductDto.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse JSON string to ProductDto", e);
        }
    }
}
