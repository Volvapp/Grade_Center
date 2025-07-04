package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByClassroomAndDayOfWeek(Classroom classroom, DayOfWeek day);
    List<Schedule> findByClassroom_SchoolAndDayOfWeek(School school, DayOfWeek dayOfWeek);
    List<Schedule> findByTeacherAndSubject(Teacher teacher, Subject subject);
}
