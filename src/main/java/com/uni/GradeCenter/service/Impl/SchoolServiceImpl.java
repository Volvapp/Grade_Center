package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.SchoolDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.SchoolService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.schoolRepository = schoolRepository;
        this.modelMapper = modelMapper;
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

    @Override
    public List<SchoolDTO> getAllSchoolDTOs() {
        List<School> schools = schoolRepository.findAll();

        return schools.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SchoolDTO convertToDTO(School school) {
        SchoolDTO dto = modelMapper.map(school, SchoolDTO.class);

        // Попълваме ръчно id-та на асоциираните ентити
        dto.setDirectorId(school.getDirector() != null ? school.getDirector().getId() : null);

        dto.setTeacherIds(
                school.getTeachers()
                        .stream()
                        .map(teacher -> teacher.getId())
                        .collect(Collectors.toList())
        );

        dto.setStudentIds(
                school.getStudents()
                        .stream()
                        .map(student -> student.getId())
                        .collect(Collectors.toList())
        );

        dto.setClassroomIds(
                school.getClassrooms()
                        .stream()
                        .map(classroom -> classroom.getId())
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
