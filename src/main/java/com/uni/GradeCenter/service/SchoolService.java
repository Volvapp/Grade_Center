package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.dto.SchoolDTO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SchoolService {
    School createSchool(School school);

    School getSchoolById(Long id);

    School updateSchool(School school);

    void deleteSchoolById(Long id);

    List<School> getAllSchools();

    void initializeSchools();

    List<SchoolDTO> getAllSchoolDTOs();

    void saveSchool(School school);

    Optional<School> findFirst();

    Optional<School> findBySchoolName(String firstLanguageSchool);
}
