package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Grade;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateGradeBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.EditGradeBindingDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

public interface GradeService {
    Grade createGrade(Grade grade);

    Grade getGradeById(Long id);

    Grade updateGrade(Grade grade);

    void deleteGradeById(Long id);

    List<Grade> getAllGrades();

    void initializeGrades();

    Map<String, Long> countGradesPerSubjectForSchool(Long id);

    Map<String, Double> averageGradesPerTeacherForSchool(Long id);

    Double calculateOverallAverageForSchool(Long id);

    void createGradeFrontend(@Valid CreateGradeBindingDTO dto, String teacherName);

    List<Grade> findByStudentAndSubject(Student student, Subject subject);

    void deleteGrade(Long gradeId);

    void updateGradeFrontend(@Valid EditGradeBindingDTO dto);
}
