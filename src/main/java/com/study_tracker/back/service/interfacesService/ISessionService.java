package com.study_tracker.back.service.interfacesService;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.sessionDto.SessionRequestDto;
import com.study_tracker.back.dto.sessionDto.SessionResponseDto;
import com.study_tracker.back.dto.sessionDto.SessionToEditRequestDto;

import java.util.List;

public interface ISessionService {

    public ApiResponse<List<SessionResponseDto>> getSessionsList();
    public ApiResponse<SessionResponseDto> getSessionbyId(Long id);
    public ApiResponse<SessionResponseDto>  createSession(SessionRequestDto sessionRequestDto);
    public ApiResponse<Void>  deleteSession(Long id);

    ApiResponse<SessionResponseDto> editSession(Long id, SessionToEditRequestDto sessionRequestDto);

    ApiResponse<SessionResponseDto> softDeleteSession(Long id);
}
