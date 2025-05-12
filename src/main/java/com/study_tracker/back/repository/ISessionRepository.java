package com.study_tracker.back.repository;

import com.study_tracker.back.entity.Session;
import com.study_tracker.back.entity.Subject;
import com.study_tracker.back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface ISessionRepository extends JpaRepository<Session, Long> {
    public List<Session> findByUser(UserEntity user);
    public List<Session> findBySubject(Subject subject);
    boolean existsByUserAndDate(UserEntity user, LocalDate date);
}
