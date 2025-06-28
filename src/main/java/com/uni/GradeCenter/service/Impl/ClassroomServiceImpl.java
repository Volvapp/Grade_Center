package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Classroom;
import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.dto.viewDTOs.ClassroomViewDTO;
import com.uni.GradeCenter.repository.ClassroomRepository;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.service.ClassroomService;
import com.uni.GradeCenter.service.SchoolService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final SchoolService schoolService;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository, SchoolService schoolService1) {
        this.classroomRepository = classroomRepository;
        this.schoolService = schoolService1;
    }

    @Override
    public Classroom createClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Classroom not found with id: " + id));
    }

    @Override
    public Classroom updateClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    @Override
    public void deleteClassroomById(Long id) {
        classroomRepository.deleteById(id);
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public void initializeClassrooms() {
        if (classroomRepository.count() > 0) return;

        Optional<School> schoolOpt1 = schoolService.findBySchoolName("First Language School");
        Optional<School> schoolOpt2 = schoolService.findBySchoolName("Second Language School");
        Optional<School> schoolOpt3 = schoolService.findBySchoolName("Third Language School");
        Optional<School> schoolOpt4 = schoolService.findBySchoolName("Fourth Language School");

        if (schoolOpt1.isEmpty() || schoolOpt2.isEmpty() || schoolOpt3.isEmpty() || schoolOpt4.isEmpty()) {
            throw new IllegalStateException("One or more schools not found. Cannot create classrooms.");
        }

        School school1 = schoolOpt1.get();
        School school2 = schoolOpt2.get();
        School school3 = schoolOpt3.get();
        School school4 = schoolOpt4.get();

        List<Classroom> classrooms = List.of(
                new Classroom("1A", 1, school1),
                new Classroom("2A", 2, school1),
                new Classroom("3A", 3, school1),

                new Classroom("4B", 4, school2),
                new Classroom("5B", 5, school2),
                new Classroom("6B", 6, school2),

                new Classroom("7C", 7, school3),
                new Classroom("8C", 8, school3),
                new Classroom("9C", 9, school3),

                new Classroom("10D", 10, school4),
                new Classroom("11D", 11, school4),
                new Classroom("12D", 12, school4)
        );

        classroomRepository.saveAll(classrooms);
    }



    @Override
    public List<Classroom> getClassroomsByIds(List<Long> classroomIds) {
        return this.classroomRepository.findAllById(classroomIds);
    }

    @Override
    public Collection<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    @Override
    public List<Classroom> findClassroomsBySchoolId(Long schoolId) {
        return this.schoolService.getSchoolById(schoolId).getClassrooms();
    }

    @Override
    public List<ClassroomViewDTO> getAllClassroomViewDTOs() {
        List<Classroom> classroomRepositoryAll = classroomRepository.findAll();
        List<ClassroomViewDTO> classroomViewDTOs = new ArrayList<>();

        for (Classroom classroom : classroomRepositoryAll) {
            ClassroomViewDTO classroomViewDTO = new ClassroomViewDTO();

            classroomViewDTO.setClassroomId(classroom.getId());
            classroomViewDTO.setClassroomName(classroom.getName());
            classroomViewDTO.setSchoolName(classroom.getSchool().getName());
            classroomViewDTO.setSchoolDirectorName(classroom.getSchool().getDirector().getFirstName() + " " + classroom.getSchool().getDirector().getLastName());

            classroomViewDTOs.add(classroomViewDTO);
        }

        return classroomViewDTOs;
    }
}
