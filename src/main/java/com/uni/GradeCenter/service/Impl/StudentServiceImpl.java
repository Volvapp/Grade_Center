package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.*;
import com.uni.GradeCenter.service.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;
    private final SchoolService schoolService;
    private final ClassroomService classroomService;
    private final ParentService parentService;

    public StudentServiceImpl(StudentRepository studentRepository, UserService userService, SchoolService schoolService, ClassroomService classroomService, ParentService parentService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
        this.schoolService = schoolService;
        this.classroomService = classroomService;
        this.parentService = parentService;
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
        User studentUser = userService.findByRole(Role.STUDENT)
                .orElseThrow(() -> new IllegalStateException("No user with role STUDENT found."));

        // Вземи училище
        School school = schoolService.getAllSchools().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No school found."));

        // Вземи клас
        Classroom classroom = classroomService.findAll().stream()
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
        User parentUser = userService.findByRole(Role.PARENT)
                .orElseThrow(() -> new IllegalStateException("No user with role PARENT found."));

        // Създай родител и го свържи със студента
        Parent parent = new Parent();
        parent.setUser(parentUser);
        parent.setChild(savedStudent);

        // Задай и родителя на студента (двупосочно)
        savedStudent.setParent(parent);

        // Запиши и двата (студентът вече е записан, сега само обновяваме)
        parentService.createParent(parent);
        studentRepository.save(savedStudent); // обновен със setParent
    }

    @Override
    public List<Student> getStudentsByIds(List<Long> studentIds) {
        return this.studentRepository.findAllById(studentIds);
    }

    @Override
    public List<Student> getStudentsByUserIds(List<Long> userIds) {
        return studentRepository.findAllByUser_IdIn(userIds);
    }

    @Override
    public void updateStudentInline(Long id, String firstName, String lastName, String email, String username, Long schoolId, Long classroomId) {
        Student student = studentRepository.findById(id).orElseThrow();
        User user = student.getUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        userService.createUser(user);

        student.setSchool(schoolService.getSchoolById(schoolId));
        student.setClassroom(classroomService.getClassroomById(classroomId));
        studentRepository.save(student);
    }

    @Override
    public void deleteByUserId(Long id) {
        this.studentRepository.deleteByUserId(id);
    }

    @Override
    public Student getStudentByUserId(Long id) {
        return this.studentRepository.findByUser_Id(id);
    }

}
