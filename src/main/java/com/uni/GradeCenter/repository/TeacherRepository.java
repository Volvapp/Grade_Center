package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    void deleteByUserId(Long id);

    Teacher findByUser_Id(Long id);
}
