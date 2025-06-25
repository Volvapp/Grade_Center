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
}
