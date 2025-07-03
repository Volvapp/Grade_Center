package com.uni.GradeCenter.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.uni.GradeCenter.repository.SubjectRepository;
import com.uni.GradeCenter.service.Impl.SubjectServiceImpl;
import com.uni.GradeCenter.service.SchoolService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Subject;

class SubjectServiceImplTest {

    private SubjectRepository subjectRepository;
    private SchoolService schoolService;
    private SubjectServiceImpl subjectService;

    @BeforeEach
    void setUp() {
        subjectRepository = mock(SubjectRepository.class);
        schoolService = mock(SchoolService.class);
        subjectService = new SubjectServiceImpl(subjectRepository, schoolService);
    }

    @Test
    void testCreateSubject() {
        Subject subject = new Subject();
        when(subjectRepository.save(subject)).thenReturn(subject);

        Subject result = subjectService.createSubject(subject);

        assertEquals(subject, result);
        verify(subjectRepository).save(subject);
    }

    @Test
    void testGetSubjectById_Found() {
        Subject subject = new Subject();
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        Subject result = subjectService.getSubjectById(1L);

        assertEquals(subject, result);
    }

    @Test
    void testGetSubjectById_NotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> subjectService.getSubjectById(1L));
    }

    @Test
    void testUpdateSubject() {
        Subject subject = new Subject();
        when(subjectRepository.save(subject)).thenReturn(subject);

        Subject result = subjectService.updateSubject(subject);

        assertEquals(subject, result);
    }

    @Test
    void testDeleteSubjectById() {
        subjectService.deleteSubjectById(5L);
        verify(subjectRepository).deleteById(5L);
    }

    @Test
    void testGetAllSubjects() {
        List<Subject> subjects = List.of(new Subject(), new Subject());
        when(subjectRepository.findAll()).thenReturn(subjects);

        List<Subject> result = subjectService.getAllSubjects();

        assertEquals(2, result.size());
    }

    @Test
    void testGetAllSubjectsByIds() {
        List<Long> ids = List.of(1L, 2L);
        List<Subject> subjects = List.of(new Subject(), new Subject());

        when(subjectRepository.findAllById(ids)).thenReturn(subjects);

        List<Subject> result = subjectService.getAllSubjectsByIds(ids);

        assertEquals(2, result.size());
    }

    @Test
    void testInitializeSubjects_SkipIfNotEmpty() {
        when(subjectRepository.count()).thenReturn(10L);

        subjectService.initializeSubjects();

        verify(subjectRepository, never()).saveAll(any());
    }

    @Test
    void testInitializeSubjects_WithMissingSchool() {
        when(subjectRepository.count()).thenReturn(0L);
        when(schoolService.findBySchoolName("First Language School")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalStateException.class, () -> subjectService.initializeSubjects());
        assertTrue(ex.getMessage().contains("School not found"));
    }

    @Test
    void testInitializeSubjects_Success() {
        when(subjectRepository.count()).thenReturn(0L);

        List<String> allSchoolNames = List.of(
                "First Language School",
                "Second Language School",
                "Third Language School",
                "Fourth Language School"
        );

        for (String name : allSchoolNames) {
            when(schoolService.findBySchoolName(name))
                    .thenReturn(Optional.of(new School()));
        }

        subjectService.initializeSubjects();

        verify(subjectRepository).saveAll(argThat(subjects -> {
            List<Subject> subjectList = new ArrayList<>();
            subjects.forEach(subjectList::add);
            return subjectList.size() == 16;
        }));
    }

    @Test
    void testFindAll() {
        List<Subject> subjects = List.of(new Subject(), new Subject());
        when(subjectRepository.findAll()).thenReturn(subjects);

        List<Subject> result = subjectService.findAll();

        assertEquals(subjects, result);
    }

    @Test
    void testGetAllSubjectsBySchoolId() {
        List<Subject> subjects = List.of(new Subject());
        when(subjectRepository.findBySchool_Id(100L)).thenReturn(subjects);

        List<Subject> result = subjectService.getAllSubjectsBySchoolId(100L);

        assertEquals(subjects, result);
    }

    @Test
    void testGetSubjectsBySchool() {
        School school = new School();
        List<Subject> subjects = List.of(new Subject());
        when(subjectRepository.findAllBySchool(school)).thenReturn(subjects);

        List<Subject> result = subjectService.getSubjectsBySchool(school);

        assertEquals(subjects, result);
    }
}

