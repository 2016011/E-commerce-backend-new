package com.example.sampleapplication.service.orderService;

import com.example.sampleapplication.dto.OrderDto;
import com.example.sampleapplication.dto.ProductOrderDto;

public interface OrderService {
    Long createOrder(OrderDto orderDto);
    Long createProductOrder(ProductOrderDto productOrderDto);
}
