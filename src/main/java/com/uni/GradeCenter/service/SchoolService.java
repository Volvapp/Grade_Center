package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.School;

import java.util.List;

public interface SchoolService {
    School createSchool(School school);

    School getSchoolById(Long id);

    School updateSchool(School school);

    void deleteSchoolById(Long id);

    List<School> getAllSchools();
}
