package com.study_tracker.back.controller;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.userDto.UserEntityResponseDto;
import com.study_tracker.back.service.interfacesService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @GetMapping("get/{id}")
    public ApiResponse<UserEntityResponseDto> getUserById(@PathVariable Long id){
        return iUserService.getUserById(id);
    }
}
