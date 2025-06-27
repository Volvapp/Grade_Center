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
import com.uni.GradeCenter.service.SchoolService;
import com.uni.GradeCenter.service.SubjectService;
import com.uni.GradeCenter.service.TeacherService;
import com.uni.GradeCenter.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserService userService;
    private final SchoolService schoolService;
    private final SubjectService subjectService;

    public TeacherServiceImpl(TeacherRepository teacherRepository, UserService userService, SchoolService schoolService, SubjectService subjectService) {
        this.teacherRepository = teacherRepository;
        this.userService = userService;
        this.schoolService = schoolService;
        this.subjectService = subjectService;
    }

    @Override
    public Teacher createTeacher(Teacher teacher) {
        return this.teacherRepository.save(teacher);
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
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
        User teacherUser = userService.findByRoleAndUsername(Role.TEACHER, "teacher")
                .orElseThrow(() -> new IllegalStateException("No user with role TEACHER found."));

        User teacherUser2 = userService.findByRoleAndUsername(Role.TEACHER, "teacher2")
                .orElseThrow(() -> new IllegalStateException("No user with role TEACHER found."));

        User teacherUser3 = userService.findByRoleAndUsername(Role.TEACHER, "teacher3")
                .orElseThrow(() -> new IllegalStateException("No user with role TEACHER found."));

        User teacherUser4 = userService.findByRoleAndUsername(Role.TEACHER, "teacher4")
                .orElseThrow(() -> new IllegalStateException("No user with role TEACHER found."));

        // Вземи училище
        School school = schoolService.getSchoolById(1L);

        Subject subject1 = subjectService.getSubjectById(1L);
        Subject subject2 = subjectService.getSubjectById(2L);
        Subject subject3 = subjectService.getSubjectById(3L);
        Subject subject4 = subjectService.getSubjectById(4L);

        Teacher teacher = new Teacher(teacherUser, school);
        teacher.setQualifiedSubjects(List.of(subject1, subject2, subject3, subject4));

        Teacher teacher2 = new Teacher(teacherUser, school);
        teacher2.setQualifiedSubjects(List.of(subject1, subject2, subject3, subject4));

        Teacher teacher3 = new Teacher(teacherUser, school);
        teacher3.setQualifiedSubjects(List.of(subject1, subject2, subject3, subject4));

        Teacher teacher4 = new Teacher(teacherUser, school);
        teacher4.setQualifiedSubjects(List.of(subject1, subject2, subject3, subject4));

        teacherRepository.saveAll(List.of(teacher, teacher2, teacher3, teacher4));
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return this.teacherRepository.findAll();
    }

    @Override
    public List<Teacher> getTeachersByIds(List<Long> teacherIds) {
        return this.teacherRepository.findAllById(teacherIds);
    }

    @Override
    public void deleteByUserId(Long id) {
        this.teacherRepository.deleteByUserId(id);
    }

    @Override
    public Teacher getTeacherByUserId(Long id) {
        return this.teacherRepository.findByUser_Id(id);
    }
}
