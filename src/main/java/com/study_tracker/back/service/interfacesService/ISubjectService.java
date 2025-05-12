package com.study_tracker.back.service.interfacesService;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.subjectDto.SubjectRequestDto;
import com.study_tracker.back.dto.subjectDto.SubjectResponseDto;
import com.study_tracker.back.dto.subjectDto.SubjectToBeListedDto;


import java.util.List;

public interface ISubjectService {
    public ApiResponse<List<SubjectToBeListedDto>> getSubjectsList();
    public ApiResponse<SubjectResponseDto> getSubjectbyId(Long id);
    public ApiResponse<SubjectResponseDto>  createSubject(SubjectRequestDto subjectRequestDto);
    public ApiResponse<Void>  deleteSubject(Long id);

    public ApiResponse<SubjectResponseDto> editSubject(Long id, SubjectRequestDto subjectRequestDto);
}
