package com.example.sampleapplication.modal;

public class Product {
    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Long stockQuantity;
    private Long soledQuantity;
    private Long imageId;


    public Product() {
    }

    public Product(Long productId, String productName, String productDescription, Double productPrice, Long stockQuantity, Long soledQuantity, Long imageId) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.stockQuantity = stockQuantity;
        this.soledQuantity = soledQuantity;
        this.imageId = imageId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Long getSoledQuantity() {
        return soledQuantity;
    }

    public void setSoledQuantity(Long soledQuantity) {
        this.soledQuantity = soledQuantity;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productPrice=" + productPrice +
                ", stockQuantity=" + stockQuantity +
                ", soledQuantity=" + soledQuantity +
                ", imageId=" + imageId +
                '}';
    }
}
