package com.uni.GradeCenter.service.impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.Impl.TeacherServiceImpl;
import com.uni.GradeCenter.service.SchoolService;
import com.uni.GradeCenter.service.SubjectService;
import com.uni.GradeCenter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceImplTest {

    @Mock private TeacherRepository teacherRepository;
    @Mock private UserService userService;
    @Mock private SchoolService schoolService;
    @Mock private SubjectService subjectService;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTeacher() {
        Teacher teacher = new Teacher();
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher result = teacherService.createTeacher(teacher);

        assertEquals(teacher, result);
        verify(teacherRepository).save(teacher);
    }

    @Test
    void testGetTeacherById_Found() {
        Teacher teacher = new Teacher();
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.getTeacherById(1L);

        assertEquals(teacher, result);
        verify(teacherRepository).findById(1L);
    }

    @Test
    void testGetTeacherById_NotFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> teacherService.getTeacherById(1L));
        assertEquals("Teacher not found with id: 1", ex.getMessage());
    }

    @Test
    void testUpdateTeacher() {
        Teacher teacher = new Teacher();
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher updated = teacherService.updateTeacher(teacher);

        assertEquals(teacher, updated);
    }

    @Test
    void testDeleteTeacherById() {
        teacherService.deleteTeacherById(1L);
        verify(teacherRepository).deleteById(1L);
    }

    @Test
    void testGetAllTeachers() {
        List<Teacher> teachers = List.of(new Teacher(), new Teacher());
        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = teacherService.getAllTeachers();

        assertEquals(2, result.size());
    }

    @Test
    void testGetTeachersByIds() {
        List<Long> ids = List.of(1L, 2L);
        List<Teacher> teachers = List.of(new Teacher(), new Teacher());

        when(teacherRepository.findAllById(ids)).thenReturn(teachers);

        List<Teacher> result = teacherService.getTeachersByIds(ids);

        assertEquals(2, result.size());
    }

    @Test
    void testDeleteByUserId() {
        teacherService.deleteByUserId(10L);
        verify(teacherRepository).deleteByUserId(10L);
    }

    @Test
    void testGetTeacherByUserId() {
        Teacher teacher = new Teacher();
        when(teacherRepository.findByUser_Id(5L)).thenReturn(teacher);

        Teacher result = teacherService.getTeacherByUserId(5L);

        assertEquals(teacher, result);
    }

    @Test
    void testFindByUsername() {
        Teacher teacher = new Teacher();
        when(teacherRepository.findByUser_Username("john")).thenReturn(teacher);

        Teacher result = teacherService.findByUsername("john");

        assertEquals(teacher, result);
    }

    @Test
    void testInitializeTeachers_Success() {
        when(teacherRepository.count()).thenReturn(0L);

        // Mock users
        List<String> usernames = List.of("teacher", "teacher1_2", "teacher2", "teacher3", "teacher4");
        for (String name : usernames) {
            User user = new User();
            user.setUsername(name);
            when(userService.findByRoleAndUsername(Role.TEACHER, name)).thenReturn(Optional.of(user));
        }

        // Mock schools
        List<String> schoolNames = List.of("First Language School", "Second Language School", "Third Language School", "Fourth Language School");
        for (String name : schoolNames) {
            when(schoolService.findBySchoolName(name)).thenReturn(Optional.of(new School()));
        }

        // Mock subjects
        List<Subject> allSubjects = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            Subject subject = new Subject();
            subject.setName("Subject " + i);
            allSubjects.add(subject);
        }

        when(subjectService.findAll()).thenReturn(allSubjects);

        teacherService.initializeTeachers();

        // Capture what was saved
        ArgumentCaptor<List<Teacher>> captor = ArgumentCaptor.forClass(List.class);
        verify(teacherRepository).saveAll(captor.capture());

        List<Teacher> savedTeachers = captor.getValue();
        assertEquals(5, savedTeachers.size());
        assertEquals(4, savedTeachers.get(0).getQualifiedSubjects().size());
    }

    @Test
    void testInitializeTeachers_AlreadyInitialized() {
        when(teacherRepository.count()).thenReturn(5L);

        teacherService.initializeTeachers();

        verifyNoMoreInteractions(userService, schoolService, subjectService);
        verify(teacherRepository, never()).saveAll(any());
    }

    @Test
    void testInitializeTeachers_NotEnoughSubjects() {
        when(teacherRepository.count()).thenReturn(0L);

        for (String name : List.of("teacher", "teacher1_2", "teacher2", "teacher3", "teacher4")) {
            when(userService.findByRoleAndUsername(Role.TEACHER, name)).thenReturn(Optional.of(new User()));
        }

        for (String name : List.of("First Language School", "Second Language School", "Third Language School", "Fourth Language School")) {
            when(schoolService.findBySchoolName(name)).thenReturn(Optional.of(new School()));
        }

        // Only 10 subjects — should fail
        List<Subject> subjects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            subjects.add(new Subject("Sub" + i, null));
        }

        when(subjectService.findAll()).thenReturn(subjects);

        assertThrows(IllegalStateException.class, () -> teacherService.initializeTeachers());
    }
}

