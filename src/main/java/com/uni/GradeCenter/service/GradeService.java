package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Grade;

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
}
