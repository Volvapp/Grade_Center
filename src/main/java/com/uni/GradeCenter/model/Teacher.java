package com.uni.GradeCenter.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher extends BaseEntity {

    @OneToOne
    private User user;

    @ManyToOne
    private School school;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Subject> qualifiedSubjects;

    @OneToMany(mappedBy = "teacher")
    private List<Grade> grades;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules;

    public Teacher(User user, School school) {
        this.user = user;
        this.school = school;
        this.qualifiedSubjects = new ArrayList<>();
        this.grades = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    public Teacher() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public List<Subject> getQualifiedSubjects() {
        return qualifiedSubjects;
    }

    public void setQualifiedSubjects(List<Subject> qualifiedSubjects) {
        this.qualifiedSubjects = qualifiedSubjects;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
