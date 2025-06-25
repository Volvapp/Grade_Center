package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Classroom;
import com.uni.GradeCenter.repository.ClassroomRepository;
import com.uni.GradeCenter.service.ClassroomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
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
}
