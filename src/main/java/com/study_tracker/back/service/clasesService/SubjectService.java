package com.study_tracker.back.service.clasesService;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.config.security.SecurityUtils;
import com.study_tracker.back.dto.subjectDto.SubjectRequestDto;
import com.study_tracker.back.dto.subjectDto.SubjectResponseDto;
import com.study_tracker.back.dto.subjectDto.SubjectToBeListedDto;
import com.study_tracker.back.entity.Session;
import com.study_tracker.back.entity.Subject;
import com.study_tracker.back.entity.UserEntity;
import com.study_tracker.back.exceptions.EntityNotFoundException;
import com.study_tracker.back.exceptions.UserNotAuthenticatedException;
import com.study_tracker.back.mappers.SubjectMapper;
import com.study_tracker.back.repository.ISessionRepository;
import com.study_tracker.back.repository.ISubjectRepository;
import com.study_tracker.back.repository.IUserRepository;
import com.study_tracker.back.service.interfacesService.ISubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Slf4j
public class SubjectService implements ISubjectService {
    @Autowired
    private ISubjectRepository iSubjectRepository;
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private ISessionRepository iSessionRepository;
    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public ApiResponse<List<SubjectToBeListedDto>> getSubjectsList() {
        // 1) Buscar el usuario desde el contexto (usuario autenticado)
        String email = SecurityUtils.getCurrentUsername();
        UserEntity user = iUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2) Obtener las subjects del usuario autenticado
        List<Subject> subjectsList = iSubjectRepository.findByUser(user);

        // 3) Mapear las subjects a dto.
        List<SubjectToBeListedDto> subjectListResponse = subjectMapper.sToSTblDto(subjectsList);
        log.info("Subject List has been obtained");
        return ApiResponse.success(subjectListResponse,"Subjects retrived successfully");
    }

    @Override
    public ApiResponse<SubjectResponseDto> getSubjectbyId(Long id) {
        Subject subject = iSubjectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, Subject.class.getSimpleName())
        );

        SubjectResponseDto subjectResponseDto = subjectMapper.sToSResDto(subject);
        log.info("Subject with ID {} has been obtained", id);
        return ApiResponse.success(subjectResponseDto, "Subject retrived successfully");
    }

    @Override
    public ApiResponse<SubjectResponseDto> createSubject(SubjectRequestDto dto) {
        // 1) Extraigo el username o lanzo UserNotAuthenticatedException
        String email = SecurityUtils.getCurrentUsername();
        log.info("el email del usuario que se intenta buscar es: " + email );
        // 2) Busco el UserEntity o lanzo EntityNotFoundException("User")
        UserEntity user = iUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User"));

        Subject subject = subjectMapper.sReqDtoToS(dto);
        user.addSubject(subject);
        Subject saved = iSubjectRepository.save(subject);
        SubjectResponseDto responseDto = subjectMapper.sToSResDto(saved);

        log.info("Subject '{}' creado para el usuario '{}'", saved.getName(), email);
        return ApiResponse.success(responseDto, "Subject created successfully");
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteSubject(Long id) {
        String email = SecurityUtils.getCurrentUsername();
        UserEntity user = iUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User"));

        Subject existingSubject = iSubjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Subject.class.getSimpleName()));

        // Validar que el Subject pertenece al User
        if (!existingSubject.getUser().getId().equals(user.getId())) {
            throw new UserNotAuthenticatedException("El Subject no pertenece al usuario autenticado");
        }

        // Eliminar todas las sessions relacionadas a este subject
        List<Session> sessionsToDelete = iSessionRepository.findBySubject(existingSubject);
        for (Session session : sessionsToDelete) {
            user.removeSession(session); // desvincular la sesión del usuario
            iSessionRepository.delete(session); // eliminar la sesión
        }

        // Ahora sí eliminar la subject
        user.removeSubject(existingSubject);
        iSubjectRepository.delete(existingSubject);

        log.info("Subject with ID {} has been deleted", id);
        return ApiResponse.success(null, "Subject deleted successfully");
    }



    @Override
    public ApiResponse<SubjectResponseDto> editSubject(Long id, SubjectRequestDto subjectRequestDto) {
        Subject existingSubject = iSubjectRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, Subject.class.getSimpleName())
        );
        existingSubject.setName(subjectRequestDto.getName());
        existingSubject.setColor(subjectRequestDto.getColor());

        Subject savedSubject = iSubjectRepository.save(existingSubject);

        SubjectResponseDto responseDto = subjectMapper.sToSResDto(savedSubject);
        log.info("Subject with ID {} has been updated", id);
        return ApiResponse.success(responseDto, "Subject updated successfully");
    }

}
