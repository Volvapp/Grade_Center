package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Classroom;
import com.uni.GradeCenter.model.Schedule;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.repository.ClassroomRepository;
import com.uni.GradeCenter.repository.ScheduleRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.ScheduleService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, TeacherRepository teacherRepository, ClassroomRepository classroomRepository) {
        this.scheduleRepository = scheduleRepository;
        this.teacherRepository = teacherRepository;
        this.classroomRepository = classroomRepository;
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

        // Вземи учител
        Teacher teacher = teacherRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No teacher found."));

        // Вземи класна стая
        Classroom classroom = classroomRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No classroom found."));

        // Вземи предмет, по който е квалифициран учителят
        Subject subject = teacher.getQualifiedSubjects().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Teacher is not qualified for any subject."));

        // Създай разписание
        Schedule schedule = new Schedule();
        schedule.setDayOfWeek(DayOfWeek.MONDAY);
        schedule.setStartTime(LocalTime.of(8, 0));
        schedule.setEndTime(LocalTime.of(8, 45));
        schedule.setTeacher(teacher);
        schedule.setClassroom(classroom);
        schedule.setSubject(subject);

        scheduleRepository.save(schedule);
    }
}
