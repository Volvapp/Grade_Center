package com.uni.GradeCenter.model.dto;

import java.util.List;

public class SchoolDTO {
    private Long id;
    private String name;
    private String address;
    private Long directorId;
    private List<Long> teacherIds;
    private List<Long> studentIds;
    private List<Long> classroomIds;

    public SchoolDTO() {}

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public List<Long> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(List<Long> teacherIds) {
        this.teacherIds = teacherIds;
    }

    public List<Long> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Long> studentIds) {
        this.studentIds = studentIds;
    }

    public List<Long> getClassroomIds() {
        return classroomIds;
    }

    public void setClassroomIds(List<Long> classroomIds) {
        this.classroomIds = classroomIds;
    }
}
