package com.example.sampleapplication.service.adminService;

import com.example.sampleapplication.dto.UserDto;
import com.example.sampleapplication.modal.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private DataSource dataSource;

    @Override
    public Long createUser(UserDto userDto){
        System.out.println(userDto);
        JdbcTemplate template = new JdbcTemplate(dataSource);
        Long nextUserId = null;
        try{
            if ( userDto.getUserId()== 0L || userDto.getUserId() == null ){
                nextUserId = getNextUserId(template);
                Long finalUserId= nextUserId;
                String code;
                if (userDto.getPassword() == null) {
                    code = null;
                } else {
                    code = (new BCryptPasswordEncoder()).encode(userDto.getPassword());
                }
                template.update("insert into users (user_id, user_name, password) VALUES (?,?,?)", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setLong(1, finalUserId);
                        ps.setString(2, userDto.getUserName());
                        ps.setString(3, code);
                    }
                });
            }else{
                nextUserId = userDto.getUserId();
                Long currentUserId = nextUserId;

                template.update("update users set user_name = ?, password = ? where user_id = ?", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, userDto.getUserName());
                        ps.setString(2, userDto.getPassword());
                        ps.setLong(3, currentUserId);
                    }
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return nextUserId;
    }

    public User getUserByUserId(Long id){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(connection -> connection.prepareStatement(
                        "select * from users where user_id = ?"),

                preparedStatment -> {
                    preparedStatment.setLong(1, id);
                },
                rs -> {
                    if(rs.next()){
                        Long userId = rs.getLong("user_id");
                        String userName = rs.getString("user_name");
                        String password = rs.getString("password");

                        return new User(userId,userName,password);
                    }else{
                        return  null;
                    }
                });

    }

    public User getUserByUsername(String name){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(connection -> connection.prepareStatement(
                        "select * from users where user_name = ?"),

                preparedStatment -> {
                    preparedStatment.setString(1, name);
                },
                rs -> {
                    if(rs.next()){
                        Long userId = rs.getLong("user_id");
                        String userName = rs.getString("user_name");
                        String password = rs.getString("password");

                        return new User(userId,userName,password);
                    }else{
                        return  null;
                    }
                });

    }

    private static Long getNextUserId(JdbcTemplate template) {
        Long userId = null;
        try {
            userId = template.query("SELECT COALESCE(MAX(user_id), 0) + 1 FROM users", new ResultSetExtractor<Long>() {
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
        return userId;
    }


}
