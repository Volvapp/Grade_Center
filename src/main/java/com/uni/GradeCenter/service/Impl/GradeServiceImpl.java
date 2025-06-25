package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Grade;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.repository.GradeRepository;
import com.uni.GradeCenter.repository.StudentRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.GradeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public GradeServiceImpl(GradeRepository gradeRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
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
        Grade existing = gradeRepository.findById(grade.getId()).orElseThrow();
        return gradeRepository.save(existing);
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

        // Вземи студент
        Student student = studentRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No student found."));

        // Вземи учител и един негов предмет
        Teacher teacher = teacherRepository.findAll().stream()
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
