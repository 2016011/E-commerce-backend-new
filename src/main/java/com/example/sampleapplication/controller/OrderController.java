package com.example.sampleapplication.controller;
import com.example.sampleapplication.dto.OrderDto;
import com.example.sampleapplication.dto.ProductOrderDto;
import com.example.sampleapplication.service.orderService.OrderService;
import com.example.sampleapplication.service.productService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")

public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @CrossOrigin
    @PostMapping("/createOrder")
    public Long createOrder(@RequestBody OrderDto orderDto) {

        Long orderId = orderService.createOrder(orderDto);
        if(!orderDto.getProductOrderList().isEmpty()){
            orderDto.getProductOrderList().forEach(productOrder -> {
                ProductOrderDto productOrderDto = new ProductOrderDto(
                        orderId,
                        productOrder.getProductId(),
                        productOrder.getOrderQuantity()

                );
                orderService.createProductOrder(productOrderDto);
                productService.updateSoledQuantity(productOrder.getProductId(), productOrder.getOrderQuantity());
            });
        }
        return orderId;
    }
}
