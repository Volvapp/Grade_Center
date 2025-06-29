package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Classroom;
import com.uni.GradeCenter.model.Schedule;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.dto.AvailableSlotDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateScheduleBindingDTO;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    Schedule createSchedule(Schedule schedule);

    Schedule getScheduleById(Long id);

    Schedule updateSchedule(Schedule schedule);

    void deleteScheduleById(Long id);

    List<Schedule> getAllSchedules();

    void initializeSchedules();

    List<Schedule> getAllByIds(List<Long> scheduleIds);

    List<LocalTime> getDailyLessonSlots();

    List<AvailableSlotDTO> getAvailableStartTimesWithTeachers(Classroom classroom, Subject subject, DayOfWeek dayOfWeek);

    void createScheduleFrontend(CreateScheduleBindingDTO dto);
}
