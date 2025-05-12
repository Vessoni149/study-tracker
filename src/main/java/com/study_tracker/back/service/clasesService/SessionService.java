package com.study_tracker.back.service.clasesService;

import com.study_tracker.back.ApiResponse;
import com.study_tracker.back.config.security.SecurityUtils;
import com.study_tracker.back.dto.sessionDto.SessionRequestDto;
import com.study_tracker.back.dto.sessionDto.SessionResponseDto;
import com.study_tracker.back.dto.sessionDto.SessionToEditRequestDto;
import com.study_tracker.back.entity.Session;
import com.study_tracker.back.entity.Subject;
import com.study_tracker.back.entity.UserEntity;
import com.study_tracker.back.exceptions.EntityNotFoundException;
import com.study_tracker.back.exceptions.SubjectsUserNotFoundException;
import com.study_tracker.back.exceptions.UserNotAuthenticatedException;
import com.study_tracker.back.mappers.SessionMapper;
import com.study_tracker.back.repository.ISessionRepository;
import com.study_tracker.back.repository.ISubjectRepository;
import com.study_tracker.back.repository.IUserRepository;
import com.study_tracker.back.service.interfacesService.ISessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Slf4j
public class SessionService  implements ISessionService {
    @Autowired
    private ISessionRepository iSessionRepository;
    @Autowired
    private ISubjectRepository iSubjectRepository;
    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private SessionMapper sessionMapper;

    @Override
    public ApiResponse<List<SessionResponseDto>> getSessionsList() {
        // 1) Buscar el usuario desde el contexto (usuario autenticado)
        String email = SecurityUtils.getCurrentUsername();
        UserEntity user = iUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // 2) Obtener las sesiones del usuario autenticado
        List<Session> sessionsList = iSessionRepository.findByUser(user);

        // 3) Mapear las sesiones a DTOs
        List<SessionResponseDto> sessionsListResponse = sessionMapper.sListToSResDtoList(sessionsList);

        // 4) Log para confirmar que las sesiones fueron obtenidas
        log.info("Session List for user '{}' has been obtained", email);

        // 5) Retornar la respuesta
        return ApiResponse.success(sessionsListResponse, "Sessions retrieved successfully");
    }

    @Override
    public ApiResponse<SessionResponseDto> getSessionbyId(Long id) {
        Session session = iSessionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, Session.class.getSimpleName())
        );
        SessionResponseDto sessionResponseDto = sessionMapper.sToSResDto(session);
        log.info("Session with ID {} has been obtained", id);
        return ApiResponse.success(sessionResponseDto, "Session retrived successfully");
    }

    @Override
    public ApiResponse<SessionResponseDto> createSession(SessionRequestDto sessionRequestDto) {
        // Convertir el DTO en una entidad
        Session session = sessionMapper.sReqDtoToS(sessionRequestDto);

        // Buscar el usuario desde el contexto (usuario autenticado)
        String email = SecurityUtils.getCurrentUsername();
        UserEntity user = iUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar la Subject por nombre para ese usuario
        String subjectName = sessionRequestDto.getSubject().getName();
        Subject subject = iSubjectRepository.findByNameAndUser(subjectName, user)
                .orElseThrow(() -> new EntityNotFoundException("Subject no encontrada para este usuario"));

        // Validar que la Subject pertenezca al usuario autenticado (esto se hace al buscarla por user)
        if (!subject.getUser().getId().equals(user.getId())) {
            throw new SubjectsUserNotFoundException("Esta materia no pertenece al usuario autenticado.");
        }

        // Asociar la Subject encontrada con la sesión
        session.setSubject(subject);
        user.addSession(session);

        // Guardar la sesión
        iSessionRepository.save(session);
        SessionResponseDto responseDto = sessionMapper.sToSResDto(session);
        return ApiResponse.success(responseDto, "Session created successfully");
    }



    @Override
    @Transactional
    public ApiResponse<Void> deleteSession(Long id) {
        String email = SecurityUtils.getCurrentUsername();
        UserEntity user = iUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User"));

        Session session = iSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Session.class.getSimpleName()));

        // Validar que la sesión pertenece al usuario autenticado
        if (!session.getUser().getId().equals(user.getId())) {
            throw new UserNotAuthenticatedException("La sesión no pertenece al usuario autenticado");
        }

        Subject subject = session.getSubject();

        // Quitar la sesión de las listas de User y Subject
        if (user != null) {
            user.getSessionList().remove(session);
        }
        if (subject != null) {
            subject.getSessions().remove(session);
        }
        // Romper la relación antes de borrar
        session.setUser(null);
        session.setSubject(null);
        // Finalmente eliminar
        iSessionRepository.delete(session);

        return ApiResponse.success(null, "Session deleted successfully");
    }


    @Override
    public ApiResponse<SessionResponseDto> editSession(Long id, SessionToEditRequestDto dto) {
        // 1) Recuperar sesión existente y usuario
        String email = SecurityUtils.getCurrentUsername();
        UserEntity user = iUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User"));
        Session session = iSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Session.class.getSimpleName()));

        // 2) Validar pertenencia
        if (!session.getUser().getId().equals(user.getId())) {
            throw new UserNotAuthenticatedException("La sesión no pertenece al usuario autenticado");
        }

        // 3) Actualizar campos básicos
        session.setDate(dto.getDate());
        session.setHours(dto.getHours());
        session.setStudyType(dto.getStudyType());

        // 4) Recuperar la Subject por ID y asignarla
        Subject subject = iSubjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException(dto.getSubjectId(), Subject.class.getSimpleName()));
        // opcional: validar que subject.getUser().getId().equals(user.getId())
        session.setSubject(subject);

        // 5) Guardar
        Session saved = iSessionRepository.save(session);

        // 6) Mapear a DTO de respuesta
        SessionResponseDto resp = sessionMapper.sToSResDto(saved);
        return ApiResponse.success(resp, "Session updated successfully");
}


    @Override
    public ApiResponse<SessionResponseDto> softDeleteSession(Long id) {
        // 1) Recupero la sesión
        String email = SecurityUtils.getCurrentUsername();
        UserEntity user = iUserRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User"));

        Session session = iSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Session.class.getSimpleName()));

        // Validar que la sesión pertenece al usuario autenticado
        if (!session.getUser().getId().equals(user.getId())) {
            throw new UserNotAuthenticatedException("La sesión no pertenece al usuario autenticado");
        }

        // 2) 'Vacío' los campos (date y id quedan intactos)
        session.setHours(0);
        session.setStudyType(null);
        // 3) Buscar la subject 'Ninguna'
        Subject none = iSubjectRepository
                .findByNameAndUser("Ninguna", user)
                .orElseThrow(() -> new EntityNotFoundException("Subject 'Ninguna' not found for user " + user.getEmail()));
        // 4) Asignarla
        session.setSubject(none);

        // 5) Guardar Session
        Session saved = iSessionRepository.save(session);

        // 6) DTO de salida
        SessionResponseDto dto = sessionMapper.sToSResDto(saved);
        return ApiResponse.success(dto, "Session soft-deleted successfully");
    }

}
