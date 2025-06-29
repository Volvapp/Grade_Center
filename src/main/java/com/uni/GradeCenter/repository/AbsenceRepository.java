package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.Absence;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    Absence findByStudentAndSubject(Student student, Subject subject);
}
