package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Grade;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.repository.GradeRepository;
import com.uni.GradeCenter.repository.StudentRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.GradeService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.TeacherService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public GradeServiceImpl(GradeRepository gradeRepository, StudentService studentService, TeacherService teacherService) {
        this.gradeRepository = gradeRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @Override
    public Grade createGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id).orElseThrow();
    }

    @Override
    public Grade updateGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public void deleteGradeById(Long id) {
        gradeRepository.deleteById(id);
    }

    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public void initializeGrades() {
        if (gradeRepository.count() > 0) return;

        Student student = studentService.getAllStudents().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No student found."));

        Teacher teacher = teacherService.getAllTeachers().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No teacher found."));

        Subject subject = teacher.getQualifiedSubjects().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Teacher has no qualified subjects."));

        Grade grade1 = new Grade();
        grade1.setStudent(student);
        grade1.setTeacher(teacher);
        grade1.setSubject(subject);
        grade1.setValue(5);
        grade1.setDate(LocalDate.now().minusDays(2));

        Grade grade2 = new Grade();
        grade2.setStudent(student);
        grade2.setTeacher(teacher);
        grade2.setSubject(subject);
        grade2.setValue(6);
        grade2.setDate(LocalDate.now().minusDays(1));

        gradeRepository.saveAll(List.of(grade1, grade2));
    }
}
