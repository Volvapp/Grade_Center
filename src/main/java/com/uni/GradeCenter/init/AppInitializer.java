package com.uni.GradeCenter.init;

import com.uni.GradeCenter.repository.*;
import com.uni.GradeCenter.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer implements CommandLineRunner {

    private final UserService userService;
    private final SchoolService schoolService;
    private final SubjectService subjectService;
    private final ClassroomService classroomService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ScheduleService scheduleService;
    private final GradeService gradeService;
    private final AbsenceService absenceService;

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final GradeRepository gradeRepository;
    private final AbsenceRepository absenceRepository;

    public AppInitializer(UserService userService,
                          SchoolService schoolService,
                          SubjectService subjectService,
                          ClassroomService classroomService,
                          TeacherService teacherService,
                          StudentService studentService,
                          ScheduleService scheduleService,
                          GradeService gradeService,
                          AbsenceService absenceService,
                          UserRepository userRepository,
                          SchoolRepository schoolRepository,
                          SubjectRepository subjectRepository,
                          ClassroomRepository classroomRepository,
                          TeacherRepository teacherRepository,
                          StudentRepository studentRepository,
                          ScheduleRepository scheduleRepository,
                          GradeRepository gradeRepository,
                          AbsenceRepository absenceRepository) {
        this.userService = userService;
        this.schoolService = schoolService;
        this.subjectService = subjectService;
        this.classroomService = classroomService;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.scheduleService = scheduleService;
        this.gradeService = gradeService;
        this.absenceService = absenceService;
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.subjectRepository = subjectRepository;
        this.classroomRepository = classroomRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.scheduleRepository = scheduleRepository;
        this.gradeRepository = gradeRepository;
        this.absenceRepository = absenceRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userService.initializeUsers();
        }

        if (schoolRepository.count() == 0) {
            schoolService.initializeSchools();
        }

        if (subjectRepository.count() == 0) {
            subjectService.initializeSubjects();
        }

        if (classroomRepository.count() == 0) {
            classroomService.initializeClassrooms();
        }

        if (teacherRepository.count() == 0) {
            teacherService.initializeTeachers();
        }

        if (studentRepository.count() == 0) {
            studentService.initializeStudents();
        }

        if (scheduleRepository.count() == 0) {
            scheduleService.initializeSchedules();
        }

        if (gradeRepository.count() == 0) {
            gradeService.initializeGrades();
        }

        if (absenceRepository.count() == 0) {
            absenceService.initializeAbsences();
        }
    }
}

