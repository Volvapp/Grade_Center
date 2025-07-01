package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.*;
import com.uni.GradeCenter.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserService userService;
    private final SchoolService schoolService;
    private final ClassroomService classroomService;
    private final ParentService parentService;

    public StudentServiceImpl(StudentRepository studentRepository, @Lazy UserService userService, SchoolService schoolService, ClassroomService classroomService, ParentService parentService) {
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
        return studentRepository.save(student);
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

        School school = schoolService.getAllSchools().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No school found."));

        Classroom classroom = classroomService.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No classroom found."));

        List<String> studentUsernames = List.of("student", "student2", "student3", "student4");
        List<String> parentUsernames = List.of("parent", "parent2", "parent3", "parent4");

        for (int i = 0; i < studentUsernames.size(); i++) {
            User studentUser = userService.findByUsername(studentUsernames.get(i));
            User parentUser = userService.findByUsername(parentUsernames.get(i));

            Student student = new Student();
            student.setUser(studentUser);
            student.setSchool(school);
            student.setClassroom(classroom);

            Student savedStudent = studentRepository.save(student);

            Parent parent = new Parent();
            parent.setUser(parentUser);
            parent.setChild(savedStudent);

            savedStudent.setParent(parent);

            parentService.createParent(parent);
            studentRepository.save(savedStudent);
        }
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
    public void updateStudentInline(Long id, String firstName, String lastName, String email, String username,
                                    Long schoolId, Long classroomId) {
        Student student = studentRepository.findById(id).orElseThrow();
        User user = student.getUser();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        userService.updateUser(user);

        student.setSchool(schoolService.getSchoolById(schoolId));

        Classroom oldClassroom = student.getClassroom();
        Classroom newClassroom = (classroomId != null) ? classroomService.getClassroomById(classroomId) : null;

        if (oldClassroom != null) {
            oldClassroom.getStudents().remove(student);
        }

        student.setClassroom(newClassroom);

        if (newClassroom != null && !newClassroom.getStudents().contains(student)) {
            newClassroom.getStudents().add(student);
        }

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
