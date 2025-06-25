package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Absence;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.repository.AbsenceRepository;
import com.uni.GradeCenter.repository.StudentRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.AbsenceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.absenceRepository = absenceRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
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
        Absence existingAbsence = absenceRepository.findById(absence.getId()).orElseThrow();
        return absenceRepository.save(existingAbsence);
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

        // Вземи студент
        Student student = studentRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No student found."));

        // Вземи учител и предмет
        Teacher teacher = teacherRepository.findAll().stream()
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
