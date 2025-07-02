package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.bindingDTOs.AbsenceCreateBindingDTO;
import com.uni.GradeCenter.repository.AbsenceRepository;
import com.uni.GradeCenter.repository.StudentRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.AbsenceService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.SubjectService;
import com.uni.GradeCenter.service.TeacherService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final SubjectService subjectService;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository, StudentService studentService, TeacherService teacherService, SubjectService subjectService) {
        this.absenceRepository = absenceRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
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
                LocalDate.now().minusDays(1),
                DayOfWeek.MONDAY,
                "08:00 - 08:45"
        );

        absenceRepository.save(absence);
    }

    @Override
    public Absence findByStudentAndSubject(Student student, Subject subject) {
        Absence absence = absenceRepository.findByStudentAndSubject(student, subject);
        return absence;
    }

    @Override
    public void createAbsenceFrontend(AbsenceCreateBindingDTO dto, String name) {
        Teacher teacher = teacherService.findByUsername(name);
        Student student = studentService.getStudentById(dto.getStudentId());
        Subject subject = subjectService.getSubjectById(dto.getSubjectId());

        Absence absence = new Absence(student, subject, teacher, dto.getDate(), DayOfWeek.valueOf(dto.getSubjectDayOfWeek()), dto.getSubjectStartEndDate());

        absenceRepository.save(absence);
    }

    @Override
    public Absence findByStudentSubjectAndScheduleInfo(Student student, Subject subject, Schedule schedule) {
        return absenceRepository.findByStudentAndSubjectAndDayOfWeekAndStartTimeSubject(student, subject, schedule.getDayOfWeek(), schedule.getStartTime() + " - " + schedule.getEndTime());
    }
}
