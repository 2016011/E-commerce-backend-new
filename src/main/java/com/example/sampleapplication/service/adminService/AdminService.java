package com.example.sampleapplication.service.adminService;

import com.example.sampleapplication.dto.OrderDto;
import com.example.sampleapplication.dto.UserDto;
import com.example.sampleapplication.modal.User;

public interface AdminService {
    Long createUser(UserDto userDto);
    User getUserByUserId (Long id);
    User getUserByUsername (String userName);
}
