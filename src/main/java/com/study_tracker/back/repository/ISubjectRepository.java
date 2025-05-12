package com.study_tracker.back.repository;

import com.study_tracker.back.entity.Subject;
import com.study_tracker.back.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISubjectRepository extends JpaRepository<Subject, Long> {
    public Optional<Subject> findByNameAndUser(String name, UserEntity user);

    public Optional<Subject> findByName(String name);
    public List<Subject> findByUser(UserEntity user);

}
