package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Classroom;
import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.repository.ClassroomRepository;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.service.ClassroomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final SchoolRepository schoolRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository, SchoolRepository schoolRepository) {
        this.classroomRepository = classroomRepository;
        this.schoolRepository = schoolRepository;
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
        Classroom existingClassroom = getClassroomById(classroom.getId());
        return classroomRepository.save(existingClassroom);
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

        Optional<School> schoolOpt = schoolRepository.findAll().stream().findFirst();

        if (schoolOpt.isEmpty()) {
            throw new IllegalStateException("No schools found! Cannot create classrooms without a school.");
        }

        School school = schoolOpt.get();

        Classroom class6A = new Classroom();
        class6A.setName("6A");
        class6A.setGrade(6);
        class6A.setSchool(school);

        Classroom class7B = new Classroom();
        class7B.setName("7B");
        class7B.setGrade(7);
        class7B.setSchool(school);

        Classroom class8C = new Classroom();
        class8C.setName("8C");
        class8C.setGrade(8);
        class8C.setSchool(school);

        classroomRepository.saveAll(List.of(class6A, class7B, class8C));
    }

    @Override
    public List<Classroom> getClassroomsByIds(List<Long> classroomIds) {
        return this.classroomRepository.findAllById(classroomIds);
    }
}
