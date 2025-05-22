package com.study_tracker.back.dto.sessionDto;

import com.study_tracker.back.dto.subjectDto.SubjectRequestDto;
import com.study_tracker.back.dto.subjectDto.SubjectResponseDto;
import com.study_tracker.back.entity.Subject;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionRequestDto {
    @NotNull(message = "El campo 'date' no puede ser 'null'")
    @PastOrPresent(message = "La fecha del campo 'date' no pede ser futura")
    private LocalDate date;


    private SubjectRequestDto subject;
    @NotNull(message = "El campo 'hours' no puede ser 'null'")
    @DecimalMin(value = "0.0", message = "La hora mínima es 0.0 horas")
    @DecimalMax(value = "24.0", message = "Las horas máximas son 24.0 horas")
    private double hours;


    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ0-9 \\-_]*$", message = "Solo se permiten letras, números, espacios, guiones y guiones bajos")
    private String studyType;
}
