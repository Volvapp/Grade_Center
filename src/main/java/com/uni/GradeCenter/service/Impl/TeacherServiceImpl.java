package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.repository.SubjectRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, UserRepository userRepository, SubjectRepository subjectRepository, SchoolRepository schoolRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.schoolRepository = schoolRepository;
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

    @Override
    public void initializeTeachers() {
        if (teacherRepository.count() > 0) return;

        // Вземи учителския User
        User teacherUser = userRepository.findByRole(Role.TEACHER)
                .orElseThrow(() -> new IllegalStateException("No user with role TEACHER found."));

        // Вземи училище
        School school = schoolRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No school found."));

        // Вземи всички предмети и избери 2
        List<Subject> subjects = subjectRepository.findAll();
        if (subjects.size() < 2) {
            throw new IllegalStateException("At least 2 subjects required to assign to teacher.");
        }

        List<Subject> qualifiedSubjects = subjects.subList(0, 2); // примерно Math и History

        Teacher teacher = new Teacher(teacherUser, school);
        teacher.setQualifiedSubjects(qualifiedSubjects);

        teacherRepository.save(teacher);
    }
}
