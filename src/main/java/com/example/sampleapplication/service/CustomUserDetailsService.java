package com.example.sampleapplication.service;

import com.example.sampleapplication.modal.User;
import com.example.sampleapplication.service.adminService.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    AdminService adminService;

    public CustomUserDetailsService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Transactional
    public UserDetails loadUserByUsername(String userName) {
        User user = this.adminService.getUserByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException(userName + " not found");
        } else {
            return UserPrincipal.create(user);
        }
    }
}