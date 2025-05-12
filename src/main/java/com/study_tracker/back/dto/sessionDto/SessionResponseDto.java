package com.study_tracker.back.dto.sessionDto;

import com.study_tracker.back.dto.subjectDto.SubjectToBeListedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class SessionResponseDto {
    private Long id;
    private LocalDate date;
    private SubjectToBeListedDto subject;
    private double hours;
    private String studyType;
}
