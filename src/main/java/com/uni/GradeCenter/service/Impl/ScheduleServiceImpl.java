package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Classroom;
import com.uni.GradeCenter.model.Schedule;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.model.dto.AvailableSlotDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateScheduleBindingDTO;
import com.uni.GradeCenter.repository.ClassroomRepository;
import com.uni.GradeCenter.repository.ScheduleRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.ClassroomService;
import com.uni.GradeCenter.service.ScheduleService;
import com.uni.GradeCenter.service.SubjectService;
import com.uni.GradeCenter.service.TeacherService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final SubjectService subjectService;
    private final TeacherRepository teacherRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, TeacherService teacherService, ClassroomService classroomService, SubjectService subjectService, TeacherRepository teacherRepository) {
        this.scheduleRepository = scheduleRepository;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
        this.subjectService = subjectService;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow();
    }

    @Override
    public Schedule updateSchedule(Schedule schedule) {
        Schedule current = scheduleRepository.findById(schedule.getId()).orElseThrow();
        return scheduleRepository.save(current);
    }

    @Override
    public void deleteScheduleById(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public void initializeSchedules() {
        if (scheduleRepository.count() > 0) return;

        Teacher teacher = teacherService.getAllTeachers().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No teacher found."));

        Classroom classroom = classroomService.getAllClassrooms().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No classroom found."));

        Subject subject = teacher.getQualifiedSubjects().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Teacher is not qualified for any subject."));

        Schedule schedule = new Schedule();
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(LocalTime.of(8, 0));
        schedule.setEndTime(LocalTime.of(8, 45));
        schedule.setTeacher(teacher);
        schedule.setClassroom(classroom);
        schedule.setSubject(subject);

        scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getAllByIds(List<Long> scheduleIds) {
        return this.scheduleRepository.findAllById(scheduleIds);
    }

    @Override
    public List<LocalTime> getDailyLessonSlots() {
        List<LocalTime> startTimes = new ArrayList<>();
        LocalTime current = LocalTime.of(8, 0);

        for (int i = 1; i <= 7; i++) {
            startTimes.add(current);
            // Почивка след 3-ти час
            if (i == 3) {
                current = current.plusMinutes(40 + 30); // 40 мин. час + 30 мин. почивка
            } else {
                current = current.plusMinutes(40 + 5);  // 40 мин. час + 5 мин. почивка
            }
        }

        return startTimes;
    }

    @Override
    public List<AvailableSlotDTO> getAvailableStartTimesWithTeachers(Classroom classroom, Subject subject, DayOfWeek dayOfWeek) {
        List<LocalTime> allSlots = getDailyLessonSlots();

        List<Teacher> qualifiedTeachers = teacherRepository
                .findBySchoolAndQualifiedSubjectsContaining(classroom.getSchool(), subject);

        List<Schedule> schedules = scheduleRepository
                .findByClassroom_SchoolAndDayOfWeek(classroom.getSchool(), dayOfWeek);

        List<AvailableSlotDTO> result = new ArrayList<>();

        for (Teacher teacher : qualifiedTeachers) {
            for (LocalTime slot : allSlots) {
                boolean isBusy = schedules.stream()
                        .anyMatch(s -> s.getTeacher().equals(teacher) && s.getStartTime().equals(slot));

                if (!isBusy) {
                    result.add(new AvailableSlotDTO(slot, teacher.getUser().getFirstName() + " " + teacher.getUser().getLastName()));
                }
            }
        }

        return result;
    }

    @Override
    public void createScheduleFrontend(CreateScheduleBindingDTO dto) {
        Classroom classroomById = classroomService.getClassroomById(dto.getClassroomId());
        Subject subjectById = subjectService.getSubjectById(dto.getSubjectId());

        String teacherFullName = dto.getTeacherName().trim();

        List<Teacher> qualifiedTeachers = teacherRepository
                .findBySchoolAndQualifiedSubjectsContaining(classroomById.getSchool(), subjectById);

        Teacher selectedTeacher = qualifiedTeachers.stream()
                .filter(t -> (t.getUser().getFirstName() + " " + t.getUser().getLastName()).equals(teacherFullName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Учителят не е намерен или не преподава този предмет."));

        if (selectedTeacher != null) {
            Schedule schedule = new Schedule();

            schedule.setClassroom(classroomById);
            schedule.setStartTime(dto.getStartTime());
            schedule.setEndTime(dto.getStartTime().plusMinutes(40));
            schedule.setDayOfWeek(dto.getDayOfWeek());
            schedule.setSubject(subjectById);
            schedule.setTeacher(selectedTeacher);

            schedule = scheduleRepository.save(schedule);

            classroomById.getSchedules().add(schedule);

            classroomService.updateClassroom(classroomById);

            selectedTeacher.getSchedules().add(schedule);

            teacherService.updateTeacher(selectedTeacher);
        } else {
            throw new RuntimeException("No teacher found.");
        }
    }

    @Override
    public List<Schedule> findByTeacherAndSubject(Teacher teacher, Subject subject) {
        return scheduleRepository.findByTeacherAndSubject(teacher, subject);
    }
}
