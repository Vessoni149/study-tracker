package com.study_tracker.back.service.interfacesService;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.authDto.AuthRequestDto;
import com.study_tracker.back.dto.authDto.AuthResponseDto;
import com.study_tracker.back.dto.authDto.RegisterRequestDto;

public interface IAuthService {
    public ApiResponse<AuthResponseDto> register (RegisterRequestDto request);
    public ApiResponse<AuthResponseDto> login(AuthRequestDto request);
}
