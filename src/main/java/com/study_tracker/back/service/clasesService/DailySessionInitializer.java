package com.study_tracker.back.service.clasesService;

import com.study_tracker.back.entity.Session;
import com.study_tracker.back.entity.Subject;
import com.study_tracker.back.entity.UserEntity;
import com.study_tracker.back.repository.ISessionRepository;
import com.study_tracker.back.repository.ISubjectRepository;
import com.study_tracker.back.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailySessionInitializer {
    @Autowired
    private ISessionRepository iSessionRepository;
    @Autowired
    private ISubjectRepository iSubjectRepository;
    @Autowired
    private IUserRepository iUserRepository;

    @Scheduled(cron = "0 50 23 * * ?")
    @Transactional
    public void createDefaultSessions() {
        LocalDate today = LocalDate.now();
        // Obtenemos la subject "Ninguna" de la BD
        Subject none = iSubjectRepository.findByName("Ninguna")
                .orElseThrow(() -> new RuntimeException("Debe existir la materia 'Ninguna'"));

        List<UserEntity> users = iUserRepository.findAll();
        for (UserEntity user : users) {
            boolean hasToday = iSessionRepository.existsByUserAndDate(user, today);
            if (!hasToday) {
                Session s = new Session();
                s.setDate(today);
                s.setSubject(none);
                s.setHours(0);
                s.setStudyType(null);
                s.setUser(user);
                iSessionRepository.save(s);
            }
        }
    }
}
