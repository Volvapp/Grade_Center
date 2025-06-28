package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Classroom;
import com.uni.GradeCenter.model.Schedule;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.repository.ClassroomRepository;
import com.uni.GradeCenter.repository.ScheduleRepository;
import com.uni.GradeCenter.repository.TeacherRepository;
import com.uni.GradeCenter.service.ClassroomService;
import com.uni.GradeCenter.service.ScheduleService;
import com.uni.GradeCenter.service.TeacherService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, TeacherService teacherService, ClassroomService classroomService) {
        this.scheduleRepository = scheduleRepository;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
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
}
