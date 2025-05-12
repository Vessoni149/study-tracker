package com.study_tracker.back.dto.authDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {
    @Email(message = "The email address must be valid and contain an '@' symbol.")
    @NotBlank(message = "'email' cannot be blank.")
    private String email;
    @NotBlank(message = "'password' cannot be blank.")
    @Pattern(
            regexp = "^[a-zA-Z0-9\\-_|°¿?%]+$",
            message = "Only letters, numbers, hyphens (-), underscores (_), pipes (|), degree signs (°), question marks (¿?), and percent signs (%) are allowed in the field 'password'."
    )
    @Size(min=8)
    private String password;

}
