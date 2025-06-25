package com.uni.GradeCenter.model.dto;

import java.util.List;

public class StudentDTO {
    private Long id;
    private Long userId;
    private Long schoolId;
    private Long classroomId;
    private Long parentId;

    // Можеш да добавиш и списъци с id-та за оценки и отсъствия ако искаш, примерно:
    // private List<Long> gradeIds;
    // private List<Long> absenceIds;

    public StudentDTO() {}

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

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
