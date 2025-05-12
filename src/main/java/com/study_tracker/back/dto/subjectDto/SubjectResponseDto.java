package com.study_tracker.back.dto.subjectDto;

import com.study_tracker.back.dto.sessionDto.SessionResponseDto;
import com.study_tracker.back.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponseDto {
    private Long id;
    private String name;
    private String color;

}
