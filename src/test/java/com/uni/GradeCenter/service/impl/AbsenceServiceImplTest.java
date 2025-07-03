package com.uni.GradeCenter.service.impl;

import com.uni.GradeCenter.model.Absence;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.model.dto.bindingDTOs.AbsenceCreateBindingDTO;
import com.uni.GradeCenter.repository.AbsenceRepository;
import com.uni.GradeCenter.service.Impl.AbsenceServiceImpl;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.SubjectService;
import com.uni.GradeCenter.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbsenceServiceImplTest {

    @Mock
    private AbsenceRepository absenceRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private AbsenceServiceImpl absenceService;

    private Student student;
    private Teacher teacher;
    private Subject subject;
    private Absence absence;

    @BeforeEach
    void setUp() {
        student = new Student();
        teacher = new Teacher();
        subject = new Subject();
        absence = new Absence(student, subject, teacher, LocalDate.of(2024, 5, 1), DayOfWeek.MONDAY, "08:00 - 08:45");
    }

    @Test
    void createAbsence_shouldSaveAndReturnAbsence() {
        when(absenceRepository.save(absence)).thenReturn(absence);

        Absence saved = absenceService.createAbsence(absence);

        assertEquals(absence, saved);
        verify(absenceRepository).save(absence);
    }

    @Test
    void getAbsenceById_shouldReturnAbsence_whenFound() {
        when(absenceRepository.findById(1L)).thenReturn(Optional.of(absence));

        Absence result = absenceService.getAbsenceById(1L);

        assertEquals(absence, result);
    }

    @Test
    void getAbsenceById_shouldThrowException_whenNotFound() {
        when(absenceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> absenceService.getAbsenceById(1L));
    }

    @Test
    void getAllAbsences_shouldReturnAll() {
        List<Absence> absences = List.of(absence);
        when(absenceRepository.findAll()).thenReturn(absences);

        List<Absence> result = absenceService.getAllAbsences();

        assertEquals(absences, result);
    }

    @Test
    void deleteAbsenceById_shouldCallRepository() {
        absenceService.deleteAbsenceById(1L);
        verify(absenceRepository).deleteById(1L);
    }

    @Test
    void findByStudentAndSubject_shouldReturnAbsence() {
        when(absenceRepository.findByStudentAndSubject(student, subject)).thenReturn(absence);

        Absence result = absenceService.findByStudentAndSubject(student, subject);

        assertEquals(absence, result);
    }

}
