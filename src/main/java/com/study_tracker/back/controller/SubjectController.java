package com.study_tracker.back.controller;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.subjectDto.SubjectRequestDto;
import com.study_tracker.back.dto.subjectDto.SubjectResponseDto;
import com.study_tracker.back.dto.subjectDto.SubjectToBeListedDto;
import com.study_tracker.back.service.interfacesService.ISubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    private ISubjectService iSubjectService;

    @GetMapping("/getAll")
    public ApiResponse<List<SubjectToBeListedDto>> getSubjects(){
        return iSubjectService.getSubjectsList();
    }

    @GetMapping("get/{id}")
    public ApiResponse<SubjectResponseDto> getSubjectById(@PathVariable Long id){
        return iSubjectService.getSubjectbyId(id);
    }

    @PostMapping("/create")
    public ApiResponse<SubjectResponseDto>  createSubject(@Valid @RequestBody SubjectRequestDto SubjectRequestDto){
        System.out.println("Recibido en el controlador: " + SubjectRequestDto);

        return iSubjectService.createSubject(SubjectRequestDto);
    }


    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void>  deleteSubject(@PathVariable Long id){
        return iSubjectService.deleteSubject(id);
    }
    @PutMapping("/edit/{id}")
    public ApiResponse<SubjectResponseDto> editSubject(@PathVariable Long id ,@RequestBody SubjectRequestDto SubjectRequestDto){
        return iSubjectService.editSubject(id, SubjectRequestDto);
    }
}
