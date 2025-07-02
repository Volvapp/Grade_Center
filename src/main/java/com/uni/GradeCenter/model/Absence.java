package com.uni.GradeCenter.model;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "absences")
public class Absence extends BaseEntity{
    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Teacher teacher;
    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time_subject")
    private String startTimeSubject;

    public Absence(Student student, Subject subject, Teacher teacher, LocalDate date, DayOfWeek dayOfWeek, String startTimeSubject) {
        this.student = student;
        this.subject = subject;
        this.teacher = teacher;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.startTimeSubject = startTimeSubject;
    }
    public Absence() {}

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTimeSubject() {
        return startTimeSubject;
    }

    public void setStartTimeSubject(String startTimeSubject) {
        this.startTimeSubject = startTimeSubject;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
