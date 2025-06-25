package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.*;
import com.uni.GradeCenter.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final ParentRepository parentRepository;
    private final ClassroomRepository classroomRepository;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository, SchoolRepository schoolRepository, ParentRepository parentRepository, ClassroomRepository classroomRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.parentRepository = parentRepository;
        this.classroomRepository = classroomRepository;
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @Override
    public Student updateStudent(Student student) {
        Student current = studentRepository.findById(student.getId()).orElseThrow();
        return studentRepository.save(current);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void initializeStudents() {
        if (studentRepository.count() > 0) return;

        // Вземи ученическия User
        User studentUser = userRepository.findByRole(Role.STUDENT)
                .orElseThrow(() -> new IllegalStateException("No user with role STUDENT found."));

        // Вземи училище
        School school = schoolRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No school found."));

        // Вземи клас
        Classroom classroom = classroomRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No classroom found."));

        // Създай студент (без родител засега)
        Student student = new Student();
        student.setUser(studentUser);
        student.setSchool(school);
        student.setClassroom(classroom);

        // Запиши студента
        Student savedStudent = studentRepository.save(student);

        // Вземи родителския User
        User parentUser = userRepository.findByRole(Role.PARENT)
                .orElseThrow(() -> new IllegalStateException("No user with role PARENT found."));

        // Създай родител и го свържи със студента
        Parent parent = new Parent();
        parent.setUser(parentUser);
        parent.setChild(savedStudent);

        // Задай и родителя на студента (двупосочно)
        savedStudent.setParent(parent);

        // Запиши и двата (студентът вече е записан, сега само обновяваме)
        parentRepository.save(parent);
        studentRepository.save(savedStudent); // обновен със setParent
    }
}
