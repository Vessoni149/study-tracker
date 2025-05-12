package com.study_tracker.back.controller;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.authDto.AuthRequestDto;
import com.study_tracker.back.dto.authDto.AuthResponseDto;
import com.study_tracker.back.dto.authDto.RegisterRequestDto;
import com.study_tracker.back.service.interfacesService.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthResponseDto> register(@RequestBody RegisterRequestDto request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request){
        return authService.login(request);
    }


}
