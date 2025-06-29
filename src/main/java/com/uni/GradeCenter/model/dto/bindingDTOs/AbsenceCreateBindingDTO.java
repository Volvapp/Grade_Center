package com.uni.GradeCenter.model.dto.bindingDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class AbsenceCreateBindingDTO {
    @NotNull
    private Long classroomId;

    @NotNull
    private Long studentId;

    @NotNull
    private Long subjectId;

    @NotBlank
    private String classroomName;

    @NotBlank
    private String studentName;

    @NotBlank
    private String subjectName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Датата не може да е в бъдещето")
    private LocalDate date;

    public AbsenceCreateBindingDTO(Long classroomId, Long studentId, Long subjectId, String classroomName, String studentName, String subjectName, LocalDate date) {
        this.classroomId = classroomId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.classroomName = classroomName;
        this.studentName = studentName;
        this.subjectName = subjectName;
        this.date = date;
    }

    public AbsenceCreateBindingDTO() {
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
