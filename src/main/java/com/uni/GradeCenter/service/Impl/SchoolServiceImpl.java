package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.SchoolDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.SchoolService;
import com.uni.GradeCenter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public SchoolServiceImpl(SchoolRepository schoolRepository, ModelMapper modelMapper,@Lazy UserService userService) {
        this.schoolRepository = schoolRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
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

        List<SchoolInitData> schoolsData = List.of(
                new SchoolInitData("director", "First Language School", "1000 Sofia, Bulgaria Blvd. 1"),
                new SchoolInitData("director2", "Second Language School", "1000 Blagoevgrad, Bulgaria Blvd. 1"),
                new SchoolInitData("director3", "Third Language School", "1000 Burgas, Bulgaria Blvd. 1"),
                new SchoolInitData("director4", "Fourth Language School", "1000 Varna, Bulgaria Blvd. 1")
        );

        List<School> schoolsToSave = new ArrayList<>();

        for (SchoolInitData data : schoolsData) {
            User director = userService.findByRoleAndUsername(Role.DIRECTOR, data.username())
                    .orElseThrow(() -> new IllegalStateException("Director '" + data.username() + "' not found"));

            School school = new School();
            school.setName(data.name());
            school.setAddress(data.address());
            school.setDirector(director);

            schoolsToSave.add(school);
        }

        schoolRepository.saveAll(schoolsToSave);
    }

    private record SchoolInitData(String username, String name, String address) {}


    @Override
    public List<SchoolDTO> getAllSchoolDTOs() {
        List<School> schools = schoolRepository.findAll();

        return schools.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void saveSchool(School school) {
        schoolRepository.save(school);
    }

    @Override
    public Optional<School> findFirst() {
        return schoolRepository.findAll().stream().findFirst();
    }

    @Override
    public Optional<School> findBySchoolName(String firstLanguageSchool) {
        return schoolRepository.findByName(firstLanguageSchool);
    }

    private SchoolDTO convertToDTO(School school) {
        SchoolDTO dto = modelMapper.map(school, SchoolDTO.class);

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
