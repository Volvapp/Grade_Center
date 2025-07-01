package com.uni.GradeCenter.service.impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.SchoolDTO;
import com.uni.GradeCenter.model.dto.viewDTOs.SubjectViewDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.service.Impl.SchoolServiceImpl;
import com.uni.GradeCenter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchoolServiceImplTest {

    private SchoolRepository schoolRepository;
    private UserService userService;
    private ModelMapper modelMapper;

    private SchoolServiceImpl schoolService;

    @BeforeEach
    void setUp() {
        schoolRepository = mock(SchoolRepository.class);
        userService = mock(UserService.class);
        modelMapper = new ModelMapper();
        schoolService = new SchoolServiceImpl(schoolRepository, modelMapper, userService);
    }

    @Test
    void testCreateSchool() {
        School school = new School();
        when(schoolRepository.save(school)).thenReturn(school);

        School result = schoolService.createSchool(school);

        assertEquals(school, result);
        verify(schoolRepository).save(school);
    }

    @Test
    void testGetSchoolByIdFound() {
        School school = new School();
        school.setId(1L);
        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));

        School result = schoolService.getSchoolById(1L);

        assertEquals(1L, result.getId());
        verify(schoolRepository).findById(1L);
    }

    @Test
    void testGetSchoolByIdNotFound() {
        when(schoolRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> schoolService.getSchoolById(2L));
    }

    @Test
    void testUpdateSchool() {
        School school = new School();
        when(schoolRepository.save(school)).thenReturn(school);

        School result = schoolService.updateSchool(school);

        assertEquals(school, result);
        verify(schoolRepository).save(school);
    }

    @Test
    void testDeleteSchoolById() {
        schoolService.deleteSchoolById(1L);
        verify(schoolRepository).deleteById(1L);
    }

    @Test
    void testFindByUserId() {
        School school = new School();
        when(schoolRepository.findByDirector_Id(10L)).thenReturn(school);

        School result = schoolService.findByUserId(10L);

        assertEquals(school, result);
    }

    @Test
    void testGetAllSchools() {
        List<School> mockSchools = List.of(new School(), new School());
        when(schoolRepository.findAll()).thenReturn(mockSchools);

        List<School> result = schoolService.getAllSchools();

        assertEquals(2, result.size());
    }

    @Test
    void testInitializeSchools_DoesNotRunIfExists() {
        when(schoolRepository.count()).thenReturn(2L);
        schoolService.initializeSchools();
        verify(schoolRepository, never()).saveAll(any());
    }

    @Test
    void testInitializeSchools_RunsAndSaves() {
        when(schoolRepository.count()).thenReturn(0L);

        for (int i = 1; i <= 4; i++) {
            User director = new User();
            director.setUsername("director" + (i == 1 ? "" : i));
            when(userService.findByRoleAndUsername(eq(Role.DIRECTOR), eq(director.getUsername())))
                    .thenReturn(Optional.of(director));
        }

        schoolService.initializeSchools();

        verify(schoolRepository).saveAll(anyList());
    }

    @Test
    void testGetAllSchoolDTOs() {
        School school = new School();
        school.setId(1L);

        User director = new User();
        director.setId(2L);
        school.setDirector(director);

        Teacher teacher = new Teacher();
        teacher.setId(3L);
        school.setTeachers(List.of(teacher));

        Student student = new Student();
        student.setId(4L);
        school.setStudents(List.of(student));

        Classroom classroom = new Classroom();
        classroom.setId(5L);
        school.setClassrooms(List.of(classroom));

        when(schoolRepository.findAll()).thenReturn(List.of(school));

        List<SchoolDTO> dtos = schoolService.getAllSchoolDTOs();

        assertEquals(1, dtos.size());
        SchoolDTO dto = dtos.get(0);
        assertEquals(2L, dto.getDirectorId());
        assertEquals(List.of(3L), dto.getTeacherIds());
        assertEquals(List.of(4L), dto.getStudentIds());
        assertEquals(List.of(5L), dto.getClassroomIds());
    }

    @Test
    void testSaveSchool() {
        School school = new School();
        schoolService.saveSchool(school);
        verify(schoolRepository).save(school);
    }

    @Test
    void testFindFirstPresent() {
        School school = new School();
        when(schoolRepository.findAll()).thenReturn(List.of(school));

        Optional<School> result = schoolService.findFirst();

        assertTrue(result.isPresent());
    }

    @Test
    void testFindFirstEmpty() {
        when(schoolRepository.findAll()).thenReturn(List.of());

        Optional<School> result = schoolService.findFirst();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindBySchoolName() {
        School school = new School();
        when(schoolRepository.findByName("First Language School")).thenReturn(Optional.of(school));

        Optional<School> result = schoolService.findBySchoolName("First Language School");

        assertTrue(result.isPresent());
    }

    @Test
    void testGetSchoolStatistics() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Grade grade = new Grade();
        grade.setValue(5.0);
        grade.setSubject(subject);

        Absence absence = new Absence();
        absence.setSubject(subject);

        Student student = new Student();
        student.setGrades(List.of(grade));
        student.setAbsences(List.of(absence));

        School school = new School();
        school.setId(1L);
        school.setSubjects(List.of(subject));
        school.setStudents(List.of(student));

        when(schoolRepository.findAll()).thenReturn(List.of(school));

        Map<Long, List<SubjectViewDTO>> stats = schoolService.getSchoolStatistics();

        assertTrue(stats.containsKey(1L));
        List<SubjectViewDTO> statList = stats.get(1L);
        assertEquals(1, statList.size());

        SubjectViewDTO stat = statList.get(0);
        assertEquals("Math", stat.getName());
        assertEquals(5.0, stat.getAverageGrade());
        assertEquals(1, stat.getTotalAbsences());
    }
}

