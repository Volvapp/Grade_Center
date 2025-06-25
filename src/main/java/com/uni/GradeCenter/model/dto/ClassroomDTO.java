package com.uni.GradeCenter.model.dto;

import java.util.List;

public class ClassroomDTO {
    private Long id;
    private String name;
    private Integer grade;
    private Long schoolId;
    private List<Long> studentIds;
    private List<Long> scheduleIds;

    public ClassroomDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public List<Long> getScheduleIds() {
        return scheduleIds;
    }

    public void setScheduleIds(List<Long> scheduleIds) {
        this.scheduleIds = scheduleIds;
    }
}
