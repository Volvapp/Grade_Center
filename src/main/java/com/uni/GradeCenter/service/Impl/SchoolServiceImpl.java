package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.service.SchoolService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public School createSchool(School school) {
        return schoolRepository.save(school);
    }

    @Override
    public School getSchoolById(Long id) {
        return schoolRepository.findById(id).orElseThrow();
    }

    @Override
    public School updateSchool(School school) {
        School current = schoolRepository.findById(school.getId()).orElseThrow();
        return schoolRepository.save(current);
    }

    @Override
    public void deleteSchoolById(Long id) {
        schoolRepository.deleteById(id);
    }

    @Override
    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }
}
