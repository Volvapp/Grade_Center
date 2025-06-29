package com.uni.GradeCenter.model.dto;

import java.time.LocalTime;

public class AvailableSlotDTO {
    private LocalTime time;
    private String teacherName;

    public AvailableSlotDTO(LocalTime time, String teacherName) {
        this.time = time;
        this.teacherName = teacherName;
    }

    public AvailableSlotDTO() {
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
