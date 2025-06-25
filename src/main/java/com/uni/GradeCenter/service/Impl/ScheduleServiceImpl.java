package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Schedule;
import com.uni.GradeCenter.repository.ScheduleRepository;
import com.uni.GradeCenter.service.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
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
}
