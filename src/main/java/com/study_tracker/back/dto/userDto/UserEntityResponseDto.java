package com.study_tracker.back.dto.userDto;

import com.study_tracker.back.dto.sessionDto.SessionResponseDto;
import com.study_tracker.back.dto.subjectDto.SubjectResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntityResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private List<SubjectResponseDto> subjects;
    private List<SessionResponseDto> sessionList;

}
