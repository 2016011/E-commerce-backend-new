package com.example.sampleapplication.modal;

public class ProductOrder {
    private Long orderId;
    private Long productId;
    private Long orderQuantity;

    public ProductOrder() {
    }

    public ProductOrder(Long orderId, Long productId, Long orderQuantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.orderQuantity = orderQuantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Long orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    @Override
    public String toString() {
        return "ProductOrder{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", orderQuantity=" + orderQuantity +
                '}';
    }
}
