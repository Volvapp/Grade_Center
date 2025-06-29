package com.uni.GradeCenter.model.dto.viewDTOs;

import java.util.ArrayList;
import java.util.List;

public class GradeEntryViewDTO {
    private String schoolName;
    private String classroomName;
    private Long classroomId;
    private String studentFullName;
    private Long studentId;
    private String subjectName;
    private Long subjectId;
    private List<GradeViewDTO> grades;

    public GradeEntryViewDTO(String schoolName, String classroomName, Long classroomId, String studentFullName, Long studentId, String subjectName, Long subjectId, List<GradeViewDTO> grades) {
        this.schoolName = schoolName;
        this.classroomName = classroomName;
        this.classroomId = classroomId;
        this.studentFullName = studentFullName;
        this.studentId = studentId;
        this.subjectName = subjectName;
        this.subjectId = subjectId;
        this.grades = grades;
    }

    public GradeEntryViewDTO() {
        this.grades = new ArrayList<>();
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public void setStudentFullName(String studentFullName) {
        this.studentFullName = studentFullName;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public List<GradeViewDTO> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeViewDTO> grades) {
        this.grades = grades;
    }
}
