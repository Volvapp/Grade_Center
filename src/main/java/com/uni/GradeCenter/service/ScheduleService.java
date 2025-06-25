package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule createSchedule(Schedule schedule);

    Schedule getScheduleById(Long id);

    Schedule updateSchedule(Schedule schedule);

    void deleteScheduleById(Long id);

    List<Schedule> getAllSchedules();

    void initializeSchedules();
}
