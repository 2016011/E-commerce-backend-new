package com.example.sampleapplication.dto;

import com.example.sampleapplication.modal.ProductOrder;

import java.util.List;

public class OrderDto {
    private Long orderId;
    private Long userId;
    private String orderConfirmation;
    private String orderStatus;
    private List<ProductOrder> productOrderList;

    public OrderDto() {
    }

    public OrderDto(Long orderId, Long userId, String orderConfirmation, String orderStatus, List<ProductOrder> productOrderList) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderConfirmation = orderConfirmation;
        this.orderStatus = orderStatus;
        this.productOrderList = productOrderList;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderConfirmation() {
        return orderConfirmation;
    }

    public void setOrderConfirmation(String orderConfirmation) {
        this.orderConfirmation = orderConfirmation;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<ProductOrder> getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(List<ProductOrder> productOrderList) {
        this.productOrderList = productOrderList;
    }


}
