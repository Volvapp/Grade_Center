package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Grade;

import java.util.List;

public interface GradeService {
    Grade createGrade(Grade grade);

    Grade getGradeById(Long id);

    Grade updateGrade(Grade grade);

    void deleteGradeById(Long id);

    List<Grade> getAllGrades();
}
