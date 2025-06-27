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
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public SchoolServiceImpl(SchoolRepository schoolRepository, ModelMapper modelMapper, UserService userService) {
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

        // Намери директор
        Optional<User> directorOpt = userService.findByRoleAndUsername(Role.DIRECTOR, "director");

        Optional<User> directorOpt2 = userService.findByRoleAndUsername(Role.DIRECTOR, "director2");

        Optional<User> directorOpt3 = userService.findByRoleAndUsername(Role.DIRECTOR, "director3");

        Optional<User> directorOpt4 = userService.findByRoleAndUsername(Role.DIRECTOR, "director4");

        if (directorOpt.isEmpty() && directorOpt2.isEmpty() && directorOpt3.isEmpty() && directorOpt4.isEmpty()) {
            throw new IllegalStateException("No user with role DIRECTOR found!");
        }

        User director = directorOpt.get();

        School school = new School();
        school.setName("First Language School");
        school.setAddress("1000 Sofia, Bulgaria Blvd. 1");
        school.setDirector(director);

        School school2 = new School();
        school.setName("Second Language School");
        school.setAddress("1000 Blagoevgrad, Bulgaria Blvd. 1");
        school.setDirector(directorOpt2.get());

        School school3 = new School();
        school.setName("Third Language School");
        school.setAddress("1000 Burgas, Bulgaria Blvd. 1");
        school.setDirector(directorOpt3.get());

        School school4 = new School();
        school.setName("Fourth Language School");
        school.setAddress("1000 Varna, Bulgaria Blvd. 1");
        school.setDirector(directorOpt4.get());

        schoolRepository.save(school);
    }

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
