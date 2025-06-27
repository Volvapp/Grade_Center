package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Classroom;
import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.repository.ClassroomRepository;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.service.ClassroomService;
import com.uni.GradeCenter.service.SchoolService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

        Optional<School> schoolOpt = schoolService.findBySchoolName("First Language School");
        Optional<School> schoolOpt2 = schoolService.findBySchoolName("Second Language School");
        Optional<School> schoolOpt3 = schoolService.findBySchoolName("Third Language School");
        Optional<School> schoolOpt4 = schoolService.findBySchoolName("Fourth Language School");

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
        // --------------------------------
        Classroom class6A2 = new Classroom();
        class6A.setName("6A");
        class6A.setGrade(6);
        class6A.setSchool(schoolOpt2.get());

        Classroom class7B2 = new Classroom();
        class7B.setName("7B");
        class7B.setGrade(7);
        class7B.setSchool(schoolOpt2.get());

        Classroom class8C2 = new Classroom();
        class8C.setName("8C");
        class8C.setGrade(8);
        class8C.setSchool(schoolOpt2.get());
        // ---------------------------------------
        Classroom class6A3 = new Classroom();
        class6A.setName("6A");
        class6A.setGrade(6);
        class6A.setSchool(schoolOpt3.get());

        Classroom class7B3 = new Classroom();
        class7B.setName("7B");
        class7B.setGrade(7);
        class7B.setSchool(schoolOpt3.get());

        Classroom class8C3 = new Classroom();
        class8C.setName("8C");
        class8C.setGrade(8);
        class8C.setSchool(schoolOpt3.get());
        // -------------------------------------
        Classroom class6A4 = new Classroom();
        class6A.setName("6A");
        class6A.setGrade(6);
        class6A.setSchool(schoolOpt4.get());

        Classroom class7B4 = new Classroom();
        class7B.setName("7B");
        class7B.setGrade(7);
        class7B.setSchool(schoolOpt4.get());

        Classroom class8C4 = new Classroom();
        class8C.setName("8C");
        class8C.setGrade(8);
        class8C.setSchool(schoolOpt4.get());

        classroomRepository.saveAll(List.of(class6A, class7B, class8C, class6A2, class7B2, class8C2, class6A3, class7B3, class8C3, class6A4, class7B4, class8C4));
    }

    @Override
    public List<Classroom> getClassroomsByIds(List<Long> classroomIds) {
        return this.classroomRepository.findAllById(classroomIds);
    }

    @Override
    public Collection<Classroom> findAll() {
        return classroomRepository.findAll();
    }
}
