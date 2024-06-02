package com.example.sampleapplication.controller;

import com.example.sampleapplication.dto.UserLoginDto;
import com.example.sampleapplication.modal.EcommerceLoginResult;
import com.example.sampleapplication.modal.User;
import com.example.sampleapplication.service.adminService.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.example.sampleapplication.util.TokenUtil.generateRefreshToken;
import static com.example.sampleapplication.util.TokenUtil.generateToken;

@Component
public class LoginHelper {
    @Autowired
    AdminService adminService;
    private static final String SUCCESS_RESPONSE = "Success";
    private static final Logger logger = LogManager.getLogger(LoginHelper.class);

    public LoginHelper(AdminService adminService){
        this.adminService = adminService;
    }

    public EcommerceLoginResult login(Authentication authentication, UserLoginDto userLoginDto){
        User user = adminService.getUserByUsername(userLoginDto.getUserName());
        return getEcommerceLoginResults(authentication, user);
    }

    public EcommerceLoginResult unlockUser(Authentication authentication, User user){
        return getEcommerceLoginResults(authentication, user);
    }

    private EcommerceLoginResult getEcommerceLoginResults(Authentication authentication, User user) {
        if (authentication.isAuthenticated()) {
            String token = generateToken(user.getUserId());
            String refreshToken = generateRefreshToken(user.getUserId());

            logger.info("GoProUserLogin Admin userId: {} token: {} ", user.getUserId(), token);
            return new EcommerceLoginResult(token, refreshToken,user.getUserId(),SUCCESS_RESPONSE);
        }else{
            logger.info("GoProUserLogin Incorrect username or password");
            return new EcommerceLoginResult(null, null, null,"Incorrect username or password");
        }
    }
}
