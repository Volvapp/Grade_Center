package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.SchoolService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    private final UserRepository userRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository, UserRepository userRepository) {
        this.schoolRepository = schoolRepository;
        this.userRepository = userRepository;
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

    @Override
    public void initializeSchools() {
        if (schoolRepository.count() > 0) return;

        // Намери директор
        Optional<User> directorOpt = userRepository.findByRole(Role.DIRECTOR);

        if (directorOpt.isEmpty()) {
            throw new IllegalStateException("No user with role DIRECTOR found!");
        }

        User director = directorOpt.get();

        School school = new School();
        school.setName("First Language School");
        school.setAddress("1000 Sofia, Bulgaria Blvd. 1");
        school.setDirector(director);

        schoolRepository.save(school);
    }
}
