package com.study_tracker.back.service.interfacesService;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.userDto.UserEntityResponseDto;


import java.util.List;

public interface IUserService {
    public ApiResponse<UserEntityResponseDto> getUserById(Long id);
}
