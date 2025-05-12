package com.study_tracker.back.dto.subjectDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectToBeListedDto {
    private Long id;
    private String name;
    private String color;
}
