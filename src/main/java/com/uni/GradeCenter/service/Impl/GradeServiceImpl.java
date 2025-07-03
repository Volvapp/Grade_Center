package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateGradeBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.EditGradeBindingDTO;
import com.uni.GradeCenter.repository.GradeRepository;
import com.uni.GradeCenter.repository.StudentRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final SubjectService subjectService;


    public GradeServiceImpl(GradeRepository gradeRepository, StudentService studentService, TeacherService teacherService, ClassroomService classroomService, SubjectService subjectService) {
        this.gradeRepository = gradeRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
        this.subjectService = subjectService;
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
        grade1.setValue(5.0);
        grade1.setDate(LocalDate.now().minusDays(2));

        Grade grade2 = new Grade();
        grade2.setStudent(student);
        grade2.setTeacher(teacher);
        grade2.setSubject(subject);
        grade2.setValue(6.0);
        grade2.setDate(LocalDate.now().minusDays(1));

        gradeRepository.saveAll(List.of(grade1, grade2));
    }

    @Override
    public Map<String, Long> countGradesPerSubjectForSchool(Long schoolId) {
        return gradeRepository.findAll().stream()
                .filter(g -> g.getStudent().getSchool().getId().equals(schoolId))
                .collect(Collectors.groupingBy(g -> g.getSubject().getName(), Collectors.counting()));
    }

    private double roundToTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public Map<String, Double> averageGradesPerTeacherForSchool(Long schoolId) {
        return gradeRepository.findAll().stream()
                .filter(g -> g.getStudent().getSchool().getId().equals(schoolId))
                .collect(Collectors.groupingBy(
                        g -> g.getTeacher().getUser().getFirstName() + " " + g.getTeacher().getUser().getLastName(),
                        Collectors.collectingAndThen(
                                Collectors.averagingDouble(Grade::getValue),
                                this::roundToTwoDecimals
                        )
                ));
    }

    @Override
    public Double calculateOverallAverageForSchool(Long schoolId) {
        return roundToTwoDecimals(
                gradeRepository.findAll().stream()
                        .filter(g -> g.getStudent().getSchool().getId().equals(schoolId))
                        .mapToDouble(Grade::getValue)
                        .average()
                        .orElse(0.0)
        );
    }


    @Override
    public void createGradeFrontend(CreateGradeBindingDTO dto, String teacherName) {
        Student student = studentService.getStudentById(dto.getStudentId());
        Subject subject = subjectService.getSubjectById(dto.getSubjectId());

        Teacher teacher = teacherService.findByUsername(teacherName);

        Grade grade = new Grade(
                student,
                subject,
                teacher,
                dto.getGrade(),
                LocalDate.now()
        );

        gradeRepository.save(grade);
    }

    @Override
    public List<Grade> findByStudentAndSubject(Student student, Subject subject) {
        List<Grade> byStudentAndSubject = gradeRepository.findByStudentAndSubject(student, subject);
        return byStudentAndSubject;
    }

    @Override
    public void deleteGrade(Long gradeId) {
        Optional<Grade> grade = gradeRepository.findById(gradeId);

        if (grade.isPresent()) {
            Teacher teacherById = teacherService.getTeacherById(grade.get().getTeacher().getId());
            teacherById.getGrades().remove(grade.get());
            teacherService.updateTeacher(teacherById);

            Student studentById = studentService.getStudentById(grade.get().getStudent().getId());
            studentById.getGrades().remove(grade.get());
            studentService.updateStudent(studentById);

            gradeRepository.deleteById(gradeId);
        } else {
            throw new IllegalStateException("No grade found with id: " + gradeId);
        }
    }

    @Override
    public void updateGradeFrontend(EditGradeBindingDTO dto) {
        Optional<Grade> byId = gradeRepository.findById(dto.getGradeId());

        if (byId.isPresent()) {
            Grade grade = byId.get();

            grade.setValue(dto.getValue());

            gradeRepository.save(grade);
        }
    }
}
