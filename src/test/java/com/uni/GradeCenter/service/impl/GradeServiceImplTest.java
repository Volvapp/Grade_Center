package com.uni.GradeCenter.service.impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateGradeBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.EditGradeBindingDTO;
import com.uni.GradeCenter.repository.GradeRepository;
import com.uni.GradeCenter.service.ClassroomService;
import com.uni.GradeCenter.service.Impl.GradeServiceImpl;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.SubjectService;
import com.uni.GradeCenter.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeServiceImplTest {

    @Mock
    private GradeRepository gradeRepository;
    @Mock private StudentService studentService;
    @Mock private TeacherService teacherService;
    @Mock private ClassroomService classroomService;
    @Mock private SubjectService subjectService;

    @InjectMocks
    private GradeServiceImpl gradeService;

    @Test
    void testCreateGrade_savesGrade() {
        Grade grade = new Grade();
        when(gradeRepository.save(grade)).thenReturn(grade);

        Grade saved = gradeService.createGrade(grade);

        assertEquals(grade, saved);
        verify(gradeRepository).save(grade);
    }

    @Test
    void testGetGradeById_existingId_returnsGrade() {
        Grade grade = new Grade();
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));

        Grade found = gradeService.getGradeById(1L);

        assertEquals(grade, found);
    }

    @Test
    void testUpdateGrade_updatesGrade() {
        Grade grade = new Grade();
        when(gradeRepository.save(grade)).thenReturn(grade);

        Grade updated = gradeService.updateGrade(grade);

        assertEquals(grade, updated);
    }

    @Test
    void testDeleteGradeById_deletesGrade() {
        gradeService.deleteGradeById(1L);
        verify(gradeRepository).deleteById(1L);
    }

    @Test
    void testGetAllGrades_returnsList() {
        List<Grade> grades = List.of(new Grade(), new Grade());
        when(gradeRepository.findAll()).thenReturn(grades);

        List<Grade> result = gradeService.getAllGrades();

        assertEquals(2, result.size());
    }

    @Test
    void testCreateGradeFrontend_createsValidGrade() {
        CreateGradeBindingDTO dto = new CreateGradeBindingDTO();
        dto.setStudentId(1L);
        dto.setSubjectId(2L);
        dto.setGrade(5.0);

        Student student = new Student();
        Subject subject = new Subject();
        Teacher teacher = new Teacher();

        when(studentService.getStudentById(1L)).thenReturn(student);
        when(subjectService.getSubjectById(2L)).thenReturn(subject);
        when(teacherService.findByUsername("teacher1")).thenReturn(teacher);

        gradeService.createGradeFrontend(dto, "teacher1");

        verify(gradeRepository).save(any(Grade.class));
    }

    @Test
    void testFindByStudentAndSubject_returnsGrades() {
        Student student = new Student();
        Subject subject = new Subject();
        List<Grade> expected = List.of(new Grade());

        when(gradeRepository.findByStudentAndSubject(student, subject)).thenReturn(expected);

        List<Grade> result = gradeService.findByStudentAndSubject(student, subject);

        assertEquals(expected, result);
    }

    @Test
    void testDeleteGrade_removesGradeFromTeacherAndStudent() {
        Grade grade = new Grade();
        grade.setId(1L);

        Teacher teacher = new Teacher();
        teacher.setId(10L); // 👈 добави ID
        teacher.setGrades(new ArrayList<>(List.of(grade)));
        grade.setTeacher(teacher);

        Student student = new Student();
        student.setId(20L); // 👈 добави ID
        student.setGrades(new ArrayList<>(List.of(grade)));
        grade.setStudent(student);

        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));
        when(teacherService.getTeacherById(10L)).thenReturn(teacher); // 👈 съвпада с ID
        when(studentService.getStudentById(20L)).thenReturn(student); // 👈 съвпада с ID

        gradeService.deleteGrade(1L);

        assertFalse(teacher.getGrades().contains(grade));
        assertFalse(student.getGrades().contains(grade));
        verify(gradeRepository).deleteById(1L);
    }


    @Test
    void testUpdateGradeFrontend_existingGrade_updatesValue() {
        EditGradeBindingDTO dto = new EditGradeBindingDTO();
        dto.setGradeId(1L);
        dto.setValue(6.0);

        Grade grade = new Grade();
        grade.setValue(4.0);

        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));

        gradeService.updateGradeFrontend(dto);

        assertEquals(6.0, grade.getValue());
        verify(gradeRepository).save(grade);
    }

    @Test
    void testAverageGradesPerTeacherForSchool_returnsCorrectMap() {
        Grade g1 = new Grade(), g2 = new Grade();
        g1.setValue(5.0);
        g2.setValue(6.0);
        g1.setStudent(mockStudentWithSchoolId(1L));
        g2.setStudent(mockStudentWithSchoolId(1L));
        g1.setTeacher(mockTeacher("John", "Smith"));
        g2.setTeacher(mockTeacher("John", "Smith"));

        when(gradeRepository.findAll()).thenReturn(List.of(g1, g2));

        Map<String, Double> result = gradeService.averageGradesPerTeacherForSchool(1L);

        assertEquals(1, result.size());
        assertEquals(5.5, result.get("John Smith"));
    }

    private Student mockStudentWithSchoolId(Long id) {
        School school = new School();
        school.setId(id);
        Student student = new Student();
        student.setSchool(school);
        return student;
    }

    private Teacher mockTeacher(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        return teacher;
    }
}

