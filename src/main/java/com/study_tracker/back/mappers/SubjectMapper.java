package com.study_tracker.back.mappers;

import com.study_tracker.back.dto.subjectDto.SubjectRequestDto;
import com.study_tracker.back.dto.subjectDto.SubjectResponseDto;
import com.study_tracker.back.dto.subjectDto.SubjectToBeListedDto;
import com.study_tracker.back.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectResponseDto sToSResDto(Subject subject);
    Subject sReqDtoToS(SubjectRequestDto subjectRequestDto);

    List<SubjectToBeListedDto> sToSTblDto(List<Subject> subjects);

}
