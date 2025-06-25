package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.TeacherService;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher createTeacher(Teacher teacher) {
        return this.teacherRepository.save(teacher);
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return this.teacherRepository.findById(id).orElseThrow();
    }

    @Override
    public Teacher updateTeacher(Teacher teacher) {
        Teacher currentTeacher = this.teacherRepository.findById(teacher.getId()).orElseThrow();
        return this.teacherRepository.save(currentTeacher);
    }

    @Override
    public void deleteTeacherById(Long id) {
        this.teacherRepository.deleteById(id);
    }
}
