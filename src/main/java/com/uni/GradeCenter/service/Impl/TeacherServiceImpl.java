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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserService userService;
    private final SchoolService schoolService;
    private final SubjectService subjectService;

    public TeacherServiceImpl(TeacherRepository teacherRepository, @Lazy UserService userService, SchoolService schoolService, SubjectService subjectService) {
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
        return this.teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacherById(Long id) {
        this.teacherRepository.deleteById(id);
    }

    @Override
    public void initializeTeachers() {
        if (teacherRepository.count() > 0) return;

        User teacherUser1 = userService.findByRoleAndUsername(Role.TEACHER, "teacher")
                .orElseThrow(() -> new IllegalStateException("User 'teacher' not found."));
        User teacherUser1_2 = userService.findByRoleAndUsername(Role.TEACHER, "teacher1_2")
                .orElseThrow(() -> new IllegalStateException("User 'teacher1_2' not found."));
        User teacherUser2 = userService.findByRoleAndUsername(Role.TEACHER, "teacher2")
                .orElseThrow(() -> new IllegalStateException("User 'teacher2' not found."));
        User teacherUser3 = userService.findByRoleAndUsername(Role.TEACHER, "teacher3")
                .orElseThrow(() -> new IllegalStateException("User 'teacher3' not found."));
        User teacherUser4 = userService.findByRoleAndUsername(Role.TEACHER, "teacher4")
                .orElseThrow(() -> new IllegalStateException("User 'teacher4' not found."));

        School school1 = schoolService.findBySchoolName("First Language School")
                .orElseThrow(() -> new IllegalStateException("School 1 not found."));
        School school2 = schoolService.findBySchoolName("Second Language School")
                .orElseThrow(() -> new IllegalStateException("School 2 not found."));
        School school3 = schoolService.findBySchoolName("Third Language School")
                .orElseThrow(() -> new IllegalStateException("School 3 not found."));
        School school4 = schoolService.findBySchoolName("Fourth Language School")
                .orElseThrow(() -> new IllegalStateException("School 4 not found."));

        List<Subject> allSubjects = subjectService.findAll(); // или subjectRepository.findAll()
        if (allSubjects.size() < 16) {
            throw new IllegalStateException("Not enough subjects to assign uniquely to each teacher.");
        }

        Teacher teacher1 = new Teacher(teacherUser1, school1);
        teacher1.setQualifiedSubjects(allSubjects.subList(0, 4));

        Teacher teacher1_2 = new Teacher(teacherUser1_2, school1);
        teacher1_2.setQualifiedSubjects(allSubjects.subList(0, 4));

        Teacher teacher2 = new Teacher(teacherUser2, school2);
        teacher2.setQualifiedSubjects(allSubjects.subList(4, 8));

        Teacher teacher3 = new Teacher(teacherUser3, school3);
        teacher3.setQualifiedSubjects(allSubjects.subList(8, 12));

        Teacher teacher4 = new Teacher(teacherUser4, school4);
        teacher4.setQualifiedSubjects(allSubjects.subList(12, 16));

        teacherRepository.saveAll(List.of(teacher1, teacher1_2, teacher2, teacher3, teacher4));
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
