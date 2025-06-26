package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Student;

import java.util.List;

public interface StudentService {
    Student createStudent(Student student);

    Student getStudentById(Long id);

    Student updateStudent(Student student);

    void deleteStudentById(Long id);

    List<Student> getAllStudents();

    void initializeStudents();

    List<Student> getStudentsByIds(List<Long> studentIds);

    List<Student> getStudentsByUserIds(List<Long> userIds);

    void updateStudentInline(Long id, String firstName, String lastName, String email, String username, Long schoolId, Long classroomId);

    void deleteByUserId(Long id);

    Student getStudentByUserId(Long id);
}
