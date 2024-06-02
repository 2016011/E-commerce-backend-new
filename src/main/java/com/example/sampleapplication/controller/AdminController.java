package com.example.sampleapplication.controller;

import com.example.sampleapplication.dto.UserDto;
import com.example.sampleapplication.dto.UserLoginDto;
import com.example.sampleapplication.modal.EcommerceLoginResult;
import com.example.sampleapplication.service.adminService.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger logger = LogManager.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    LoginHelper loginHelper;

    private static final String SUCCESS_RESPONSE = "Success";
    private static final String FAILED_RESPONSE = "Failed";
    private static final String INCORRECT_USERNAME_PASSWORD = "Incorrect username or password";

    @CrossOrigin
    @PostMapping("/createUser")
    public Long createUser(@RequestBody UserDto userDto) {
        return adminService.createUser(userDto);
    }

    @CrossOrigin
    @PostMapping("/userLogin")
    public EcommerceLoginResult userLogin(@RequestBody UserLoginDto userLoginDto){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUserName(), userLoginDto.getPassword()));
            return loginHelper.login(authentication, userLoginDto);
        } catch (BadCredentialsException badCredentialsException) {
            logger.error("goProUserLogin badCredentialsException : {}", badCredentialsException.getMessage());
            badCredentialsException.printStackTrace();
            logger.error("goProUserLogin Authentication for {} failed", userLoginDto.getUserName());
            return new EcommerceLoginResult(null,null,null,INCORRECT_USERNAME_PASSWORD);
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            return new EcommerceLoginResult(null,null,null,e.getMessage());
        }
    }

}
