package com.example.sampleapplication.service.orderService;
import com.example.sampleapplication.dto.OrderDto;
import com.example.sampleapplication.dto.ProductOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DataSource dataSource;

    @Override
    public Long createOrder(OrderDto orderDto){
        System.out.println(orderDto);
        JdbcTemplate template = new JdbcTemplate(dataSource);
        Long nextOrderId = null;
        try{
            if ( orderDto.getOrderId()== 0L || orderDto.getOrderId() == null ){
                nextOrderId = getNextOrderId(template);
                Long finalOrderId= nextOrderId;
                orderDto.setOrderConfirmation("CONFIRMED");
                orderDto.setOrderStatus("PENDING");
                template.update("insert into orders (order_id, user_id, order_confirmation, status) VALUES (?,?,?,?)", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, finalOrderId);
                        ps.setLong(2, orderDto.getUserId());
                        ps.setString(3, orderDto.getOrderConfirmation());
                        ps.setString(4, orderDto.getOrderStatus());
                    }
                });
            }else{
                nextOrderId = orderDto.getOrderId();
                Long currentOrderId = nextOrderId;

                template.update("update orders set user_id = ?, order_confirmation = ?, status = ? where order_id = ?", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, orderDto.getUserId());
                        ps.setString(2, orderDto.getOrderConfirmation());
                        ps.setString(3, orderDto.getOrderStatus());
                        ps.setLong(4, currentOrderId);
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return nextOrderId;
    }

    public Long createProductOrder(ProductOrderDto productOrderDto){
        System.out.println(productOrderDto);
        JdbcTemplate template = new JdbcTemplate(dataSource);
        try{
                template.update("insert into product_order (order_id, product_id, order_quantity) VALUES (?,?,?)", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, productOrderDto.getOrderId());
                        ps.setLong(2, productOrderDto.getProductId());
                        ps.setLong(3, productOrderDto.getOrderQuantity());
                    }
                });
        }catch(Exception e){
            e.printStackTrace();
        }
        return productOrderDto.getOrderId();
    }

    private static Long getNextOrderId(JdbcTemplate template) {
        Long orderId = null;
        try {
            orderId = template.query("SELECT COALESCE(MAX(order_id), 0) + 1 FROM orders", new ResultSetExtractor<Long>() {
                @Override
                public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Long id = 1L; // Default to 1 if the table is empty
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                    return id;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderId;
    }
}
