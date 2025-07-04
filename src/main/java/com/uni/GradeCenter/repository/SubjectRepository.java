package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findBySchool_Id(Long schoolId);

    List<Subject> findAllBySchool(School school);
}
