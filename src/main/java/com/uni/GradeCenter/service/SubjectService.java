package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Subject;

import java.util.List;

public interface SubjectService {
    Subject createSubject(Subject subject);

    Subject getSubjectById(Long id);

    Subject updateSubject(Subject subject);

    void deleteSubjectById(Long id);

    List<Subject> getAllSubjects();

    List<Subject> getAllSubjectsByIds(List<Long> ids);

    void initializeSubjects();

    List<Subject> findAll();

    List<Subject> getAllSubjectsBySchoolId(Long id);

    List<Subject> getSubjectsBySchool(School school);
}
