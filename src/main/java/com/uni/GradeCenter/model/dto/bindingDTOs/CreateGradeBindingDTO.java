package com.uni.GradeCenter.model.dto.bindingDTOs;

import jakarta.validation.constraints.*;

public class CreateGradeBindingDTO {
    @NotNull
    private Long classroomId;

    @NotNull
    private String classroomName;

    @NotNull
    private Long studentId;

    @NotNull
    private String studentName;

    @NotNull
    private Long subjectId;

    @NotNull
    private String subjectName;

    @DecimalMin(value = "2.00", message = "Оценката трябва да е поне 2.00")
    @DecimalMax(value = "6.00", message = "Оценката трябва да е най-много 6.00")
    @Digits(integer = 1, fraction = 2, message = "Оценката трябва да има до две цифри след десетичната запетая")
    private Double grade;

    public CreateGradeBindingDTO(Long classroomId, String classroomName, Long studentId, String studentName, Long subjectId, String subjectName, Double grade) {
        this.classroomId = classroomId;
        this.classroomName = classroomName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.grade = grade;
    }

    public CreateGradeBindingDTO() {
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
