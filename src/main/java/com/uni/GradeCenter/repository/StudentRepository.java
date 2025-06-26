package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByUser_IdIn(List<Long> userIds);

    void deleteByUserId(Long id);

    Student findByUser_Id(Long id);
}
