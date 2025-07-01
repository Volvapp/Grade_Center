package com.uni.GradeCenter.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.uni.GradeCenter.service.Impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.repository.StudentRepository;
import com.uni.GradeCenter.service.*;

class StudentServiceImplTest {

    private StudentRepository studentRepository;
    private UserService userService;
    private SchoolService schoolService;
    private ClassroomService classroomService;
    private ParentService parentService;

    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        userService = mock(UserService.class);
        schoolService = mock(SchoolService.class);
        classroomService = mock(ClassroomService.class);
        parentService = mock(ParentService.class);

        studentService = new StudentServiceImpl(studentRepository, userService, schoolService, classroomService, parentService);
    }

    @Test
    void testCreateStudent() {
        Student student = new Student();
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.createStudent(student);

        assertEquals(student, result);
        verify(studentRepository).save(student);
    }

    @Test
    void testGetStudentById_Found() {
        Student student = new Student();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertEquals(student, result);
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> studentService.getStudentById(1L));
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student();
        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.updateStudent(student);

        assertEquals(student, result);
    }

    @Test
    void testDeleteStudentById() {
        studentService.deleteStudentById(5L);
        verify(studentRepository).deleteById(5L);
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = List.of(new Student(), new Student());
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertEquals(2, result.size());
    }

    @Test
    void testInitializeStudents_SkipIfExists() {
        when(studentRepository.count()).thenReturn(5L);
        studentService.initializeStudents();
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testInitializeStudents_MissingSchool() {
        when(studentRepository.count()).thenReturn(0L);
        when(schoolService.getAllSchools()).thenReturn(List.of());

        Exception ex = assertThrows(IllegalStateException.class, () -> studentService.initializeStudents());
        assertEquals("No school found.", ex.getMessage());
    }

    @Test
    void testInitializeStudents_MissingClassroom() {
        when(studentRepository.count()).thenReturn(0L);
        when(schoolService.getAllSchools()).thenReturn(List.of(new School()));
        when(classroomService.findAll()).thenReturn(List.of());

        Exception ex = assertThrows(IllegalStateException.class, () -> studentService.initializeStudents());
        assertEquals("No classroom found.", ex.getMessage());
    }

    @Test
    void testInitializeStudents_Success() {
        when(studentRepository.count()).thenReturn(0L);
        School school = new School();
        Classroom classroom = new Classroom();
        classroom.setStudents(new ArrayList<>());

        when(schoolService.getAllSchools()).thenReturn(List.of(school));
        when(classroomService.findAll()).thenReturn(List.of(classroom));

        for (int i = 1; i <= 4; i++) {
            User studentUser = new User();
            User parentUser = new User();
            when(userService.findByUsername("student" + (i == 1 ? "" : i))).thenReturn(studentUser);
            when(userService.findByUsername("parent" + (i == 1 ? "" : i))).thenReturn(parentUser);
            when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));
        }

        studentService.initializeStudents();

        verify(studentRepository, times(8)).save(any(Student.class)); // 4 saves initially + 4 after setting parent
        verify(parentService, times(4)).createParent(any(Parent.class));
    }

    @Test
    void testGetStudentsByIds() {
        List<Long> ids = List.of(1L, 2L);
        List<Student> students = List.of(new Student(), new Student());

        when(studentRepository.findAllById(ids)).thenReturn(students);

        List<Student> result = studentService.getStudentsByIds(ids);

        assertEquals(2, result.size());
    }

    @Test
    void testGetStudentsByUserIds() {
        List<Long> ids = List.of(10L, 20L);
        List<Student> students = List.of(new Student());

        when(studentRepository.findAllByUser_IdIn(ids)).thenReturn(students);

        List<Student> result = studentService.getStudentsByUserIds(ids);

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateStudentInline() {
        Long studentId = 1L;
        Long schoolId = 10L;
        Long newClassroomId = 100L;

        // Mock entities
        User user = new User();
        Student student = new Student();
        student.setUser(user);
        student.setClassroom(new Classroom());
        student.getClassroom().setStudents(new ArrayList<>(List.of(student)));

        School school = new School();
        Classroom newClassroom = new Classroom();
        newClassroom.setStudents(new ArrayList<>());

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(schoolService.getSchoolById(schoolId)).thenReturn(school);
        when(classroomService.getClassroomById(newClassroomId)).thenReturn(newClassroom);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        studentService.updateStudentInline(studentId, "John", "Doe", "john@example.com", "johnny",
                schoolId, newClassroomId);

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("johnny", user.getUsername());

        assertEquals(school, student.getSchool());
        assertEquals(newClassroom, student.getClassroom());
        assertTrue(newClassroom.getStudents().contains(student));
        assertFalse(student.getClassroom() == null || student.getClassroom().getStudents().contains(null));

        verify(userService).updateUser(user);
    }

    @Test
    void testUpdateStudentInline_NoNewClassroom() {
        Long studentId = 2L;
        Long schoolId = 20L;

        Student student = new Student();
        student.setUser(new User());

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(schoolService.getSchoolById(schoolId)).thenReturn(new School());

        studentService.updateStudentInline(studentId, "A", "B", "email", "user", schoolId, null);

        assertNull(student.getClassroom());
    }

    @Test
    void testDeleteByUserId() {
        studentService.deleteByUserId(7L);
        verify(studentRepository).deleteByUserId(7L);
    }

    @Test
    void testGetStudentByUserId() {
        Student student = new Student();
        when(studentRepository.findByUser_Id(9L)).thenReturn(student);

        Student result = studentService.getStudentByUserId(9L);

        assertEquals(student, result);
    }
}

