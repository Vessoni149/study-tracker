package com.study_tracker.back.service.clasesService;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.dto.authDto.AuthRequestDto;
import com.study_tracker.back.dto.authDto.AuthResponseDto;
import com.study_tracker.back.dto.authDto.RegisterRequestDto;
import com.study_tracker.back.entity.Role;
import com.study_tracker.back.entity.Subject;
import com.study_tracker.back.entity.UserEntity;
import com.study_tracker.back.exceptions.EmailAlreadyUsedException;
import com.study_tracker.back.repository.IUserRepository;
import com.study_tracker.back.service.interfacesService.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public ApiResponse<AuthResponseDto> register (RegisterRequestDto request){
        // 1. Verificar si ya existe un usuario con el mismo email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException("El email ya está registrado");
        }
        UserEntity user = UserEntity.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        // Inicializo manualmente la lista (porque el builder no lo hizo)
        if (user.getSubjects() == null) {
            user.setSubjects(new ArrayList<>());
        }

        //Le asigno a todos los usuarios creados la Subject 'Ninguna'.
        Subject none = new Subject();
        none.setName("Ninguna");
        none.setColor("#000");
        none.setUser(user);
        user.getSubjects().add(none);

        userRepository.save(user);
        //Ahora hay que generar el token para devolver en la respuesta.
        var jwtToken = jwtService.generateToken(user); //usamos el método generateToken que requiere un UserDetails en parámetro.
        AuthResponseDto  response = AuthResponseDto.builder()
                .fullName(request.getFullName())
                .token(jwtToken).build();

        return ApiResponse.success(response, "Register successfull");
        //en este caso solo se construye con el token porque el authResponse solo tiene ese atributo, si le especificamos más hay que ponerlos dentro del builder.
    }

    @Override
    public ApiResponse<AuthResponseDto> login(AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        AuthResponseDto response = AuthResponseDto.builder()
                .token(jwtToken)
                .fullName(user.getFullName())
                .build();
        return ApiResponse.success(response, "Login successful");
    }

}
