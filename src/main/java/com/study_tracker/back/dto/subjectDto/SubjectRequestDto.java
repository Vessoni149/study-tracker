package com.study_tracker.back.dto.subjectDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequestDto {
    @NotBlank(message = "El campo 'name' no debe estar vacío")
    @NotNull(message = "El campo 'name' no puede ser 'null'")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ0-9 \\-_]*$", message = "Solo se permiten letras, números, espacios, guiones y guiones bajos")
    private String name;
    @NotBlank(message = "El campo 'color' no debe estar vacío")
    @NotNull(message = "El campo 'color' no puede ser 'null'")
    @Pattern(regexp = "^[a-zA-Z0-9 \\-_#%]*$", message = "Solo se permiten letras, números, espacios, guiones y guiones bajos")
    private String color;
}
