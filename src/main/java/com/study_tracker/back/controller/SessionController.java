package com.study_tracker.back.controller;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.sessionDto.SessionRequestDto;
import com.study_tracker.back.dto.sessionDto.SessionResponseDto;
import com.study_tracker.back.dto.sessionDto.SessionToEditRequestDto;
import com.study_tracker.back.service.interfacesService.ISessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private ISessionService iSessionService;

    @GetMapping("/getAll")
    public ApiResponse<List<SessionResponseDto>> getSessions(){
        return iSessionService.getSessionsList();
    }

    @GetMapping("get/{id}")
    public ApiResponse<SessionResponseDto> getSessionById(@PathVariable Long id){
    return iSessionService.getSessionbyId(id);
    }

    @PostMapping("/create")
    public ApiResponse<SessionResponseDto>  createSession(@Valid @RequestBody SessionRequestDto sessionRequestDto){
        System.out.println("Recibido en el controlador: " + sessionRequestDto);

        return iSessionService.createSession(sessionRequestDto);
    }
    @PatchMapping("/soft-delete/{id}")
    public ApiResponse<SessionResponseDto> softDelete(@PathVariable Long id) {
        return iSessionService.softDeleteSession(id);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void>  deleteSession(@PathVariable Long id){
        return iSessionService.deleteSession(id);
    }
    @PutMapping("/edit/{id}")
    public ApiResponse<SessionResponseDto> editSession(@PathVariable Long id ,@RequestBody SessionToEditRequestDto sessionRequestDto){
        return iSessionService.editSession(id, sessionRequestDto);
    }

}
