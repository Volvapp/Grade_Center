package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Absence;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.repository.AbsenceRepository;
import com.uni.GradeCenter.repository.StudentRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.AbsenceService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.TeacherService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository, StudentService studentService, TeacherService teacherService) {
        this.absenceRepository = absenceRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @Override
    public Absence createAbsence(Absence absence) {
        return absenceRepository.save(absence);
    }

    @Override
    public Absence getAbsenceById(Long id) {
        return absenceRepository.findById(id).orElseThrow();
    }

    @Override
    public Absence updateAbsence(Absence absence) {
        return absenceRepository.save(absence);
    }

    @Override
    public void deleteAbsenceById(Long id) {
        absenceRepository.deleteById(id);
    }

    @Override
    public List<Absence> getAllAbsences() {
        return absenceRepository.findAll();
    }

    @Override
    public void initializeAbsences() {
        if (absenceRepository.count() > 0) return;

        Student student = studentService.getAllStudents().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No student found."));


        Teacher teacher = teacherService.getAllTeachers().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No teacher found."));

        Subject subject = teacher.getQualifiedSubjects().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Teacher has no qualified subjects."));

        Absence absence = new Absence(
                student,
                subject,
                teacher,
                LocalDate.now().minusDays(1)
        );

        absenceRepository.save(absence);
    }
}
