package com.uni.GradeCenter.model.dto;

import java.util.List;

public class TeacherDTO {

    private Long id;
    private Long userId;
    private Long schoolId;
    private List<Long> qualifiedSubjectIds;
    private List<Long> gradeIds;
    private List<Long> scheduleIds;

    public TeacherDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public List<Long> getQualifiedSubjectIds() {
        return qualifiedSubjectIds;
    }

    public void setQualifiedSubjectIds(List<Long> qualifiedSubjectIds) {
        this.qualifiedSubjectIds = qualifiedSubjectIds;
    }

    public List<Long> getGradeIds() {
        return gradeIds;
    }

    public void setGradeIds(List<Long> gradeIds) {
        this.gradeIds = gradeIds;
    }

    public List<Long> getScheduleIds() {
        return scheduleIds;
    }

    public void setScheduleIds(List<Long> scheduleIds) {
        this.scheduleIds = scheduleIds;
    }
}
