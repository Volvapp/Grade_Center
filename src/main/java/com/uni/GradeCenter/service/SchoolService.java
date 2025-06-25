package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.dto.SchoolDTO;

import java.util.List;

public interface SchoolService {
    School createSchool(School school);

    School getSchoolById(Long id);

    School updateSchool(School school);

    void deleteSchoolById(Long id);

    List<School> getAllSchools();

    void initializeSchools();

    List<SchoolDTO> getAllSchoolDTOs();

    void saveSchool(School school);
}
