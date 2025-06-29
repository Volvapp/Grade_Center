package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.Grade;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentAndSubject(Student student, Subject subject);
}
