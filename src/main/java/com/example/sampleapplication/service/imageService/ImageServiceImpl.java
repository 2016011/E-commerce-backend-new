package com.example.sampleapplication.service.imageService;

import com.example.sampleapplication.dto.ImageDto;
import com.example.sampleapplication.modal.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private DataSource dataSource;

    @Override
    public Long storeImage(ImageDto imageDto){
        System.out.println(imageDto);
        JdbcTemplate template = new JdbcTemplate(dataSource);
        Long nextImageId = null;
        try{
            if ( imageDto.getImageId()== 0L || imageDto.getImageId() == null ){
                nextImageId = getNextImageId(template);
                Long finalImageId = nextImageId;

                template.update("insert into image (image_id, image_data) VALUES (?,?)", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, finalImageId);
                        ps.setBytes(2, imageDto.getImageData());
                    }
                });
            }else{
                nextImageId = imageDto.getImageId();
                Long currentImageId = nextImageId;

                template.update("update product (set image_data = ? where image_id = ?)", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setBytes(1, imageDto.getImageData());
                        ps.setLong(2, currentImageId);
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return nextImageId;
    }
    public HttpEntity<byte[]> getImage(Long imageId){
        JdbcTemplate template = new JdbcTemplate(dataSource);
        Image result = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        String sql = "SELECT * FROM image WHERE image_id = ?";
        Image image = template.queryForObject(sql, new Object[]{imageId}, new BeanPropertyRowMapper<>(Image.class));

        System.out.println(image);
        if(image != null){
            result = new Image(image.getImageId(), image.getImageData());
        }
        if (result != null) {
            headers.setContentLength((long)result.getImageData().length);
            return new HttpEntity<>(result.getImageData(), headers);
        } else {
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new HttpEntity<>("OK".getBytes(), headers);
        }
    }

    public String deleteImage (Long id){
        JdbcTemplate template = new JdbcTemplate(dataSource);
        int delete = 0;
        try {
            delete = template.update("DELETE FROM image WHERE image_id = ?", id);

            if (delete > 0) {
                return "image with ID " + id + " deleted successfully.";
            } else {
                return "Image with ID " + id + " not found or not deleted.";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "An error occurred while trying to delete Image with ID " + id;
    }

    private static Long getNextImageId(JdbcTemplate template) {
        Long imageId = null;
        try {
            imageId = template.query("SELECT COALESCE(MAX(image_id), 0) + 1 FROM image", new ResultSetExtractor<Long>() {
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
        return imageId;
    }

}
