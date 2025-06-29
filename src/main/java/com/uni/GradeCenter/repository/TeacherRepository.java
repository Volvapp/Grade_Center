package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    void deleteByUserId(Long id);

    Teacher findByUser_Id(Long id);

    List<Teacher> findBySchoolAndQualifiedSubjectsContaining(School school, Subject subject);
}
