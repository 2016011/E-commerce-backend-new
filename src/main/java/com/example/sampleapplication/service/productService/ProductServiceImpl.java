package com.example.sampleapplication.service.productService;
import com.example.sampleapplication.dto.ImageDto;
import com.example.sampleapplication.dto.ProductDto;
import com.example.sampleapplication.modal.Product;
import com.example.sampleapplication.service.imageService.ImageService;
import com.example.sampleapplication.util.ImageEditor;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ImageService imageService;
    @Override
    public Long createProducts(ProductDto productDto){
        JdbcTemplate template = new JdbcTemplate(dataSource);
        Long productId = null;
        try{
            if(productDto.getFile() != null){
                byte[] fileBytesArray = new ImageEditor().toByteArray(productDto.getFile());
                ImageDto image = new ImageDto(0L, fileBytesArray);
                Long imageId = imageService.storeImage(image);
                productDto.setImageId(imageId);
            }
            if (productDto.getProductId() == 0L || productDto.getProductId() == null ){
                productId = getNextProductId(template);
                Long finalProductId = productId;
                productDto.setSoledQuantity(0L);

                template.update("insert into product (product_id, product_name, product_description, product_price, stock_quantity, soled_quantity, image_id) VALUES (?, ?, ?, ?, ?, ?,?)", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, finalProductId);
                        ps.setString(2, productDto.getProductName());
                        ps.setString(3, productDto.getProductDescription());
                        ps.setDouble(4, productDto.getProductPrice());
                        ps.setLong(5, productDto.getStockQuantity());
                        ps.setLong(6, productDto.getSoledQuantity());
                        ps.setLong(7, productDto.getImageId());
                    }
                });
            }else{
                productId = productDto.getProductId();
                Long alreadyProductId = productId;

                template.update("update product set product_name = ?, product_description = ?, product_price = ?, stock_quantity = ?, soled_quantity = ?, image_id = ? where product_id = ?", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, productDto.getProductName());
                        ps.setString(2, productDto.getProductDescription());
                        ps.setDouble(3, productDto.getProductPrice());
                        ps.setLong(4, productDto.getStockQuantity());
                        ps.setLong(5, productDto.getSoledQuantity());
                        ps.setLong(6, productDto.getImageId());
                        ps.setLong(7, alreadyProductId);
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return productId;
    }


    public List<Product> getProducts(){
        JdbcTemplate template = new JdbcTemplate(dataSource);
        List<Product> productList = null;

        try{
            productList = template.query("select * from product", new ResultSetExtractor<List<Product>>() {
                @Override
                public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    ArrayList<Product> products = new ArrayList<>();
                    while (rs.next()) {
                        products.add(new Product(
                                rs.getLong("product_id"),
                                rs.getString("product_name"),
                                rs.getString("product_description"),
                                rs.getDouble("product_price"),
                                rs.getLong("stock_quantity"),
                                rs.getLong("soled_quantity"),
                                rs.getLong("image_id")
                        ));
                    }
                    return products;
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    public Product getProductByProductId(Long productId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(connection -> connection.prepareStatement(
                        "select * from product where product_id = ?"),
                preparedStatement -> {
                    preparedStatement.setLong(1, productId);
                },
                rs -> {
                    if (rs.next()) {
                                Long id = rs.getLong("product_id");
                                String productName = rs.getString("product_name");
                                String productDesc = rs.getString("product_description");
                                Double productPrice = rs.getDouble("product_price");
                                Long stockQuantity = rs.getLong("stock_quantity");
                                Long soledQuantity = rs.getLong("soled_quantity");
                                Long imageId = rs.getLong("image_id");

                        return new Product(id, productName, productDesc, productPrice, stockQuantity, soledQuantity, imageId);
                    } else {
                        return null;
                    }
                });
    }

    public String deleteProduct (Long productId, Long imageId){
        JdbcTemplate template = new JdbcTemplate(dataSource);

        int delete = 0;
        try {
            delete = template.update("DELETE FROM product WHERE product_id = ?", productId);
            if (delete > 0) {
                imageService.deleteImage(imageId);
                return "Product with ID " + productId + " deleted successfully.";
            } else {
                return "Product with ID " + productId + " not found or not deleted.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "An error occurred while trying to delete Product with ID " + productId;
    }

    public void updateSoledQuantity(Long productId, Long soledQuantity){
        Product product = getProductByProductId(productId);

        if (product != null) {
            Long currentSoledQuantity = product.getSoledQuantity() != null ? product.getSoledQuantity() : 0 ;
            product.setSoledQuantity(currentSoledQuantity + soledQuantity);
            ProductDto productDto = new ProductDto(product.getProductId(), product.getProductName(), product.getProductDescription(), product.getProductPrice(), product.getStockQuantity(), product.getSoledQuantity(), product.getImageId());
            createProducts(productDto);
        } else {
            throw new RuntimeException("Product not found with id: " + productId);
        }
    }

    private static Long getNextProductId(JdbcTemplate template) {
        Long productId = null;
        try {
            productId = template.query("SELECT COALESCE(MAX(product_id), 0) + 1 FROM product", new ResultSetExtractor<Long>() {
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
        return productId;
    }


}
