package com.uni.GradeCenter.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student extends BaseEntity {

    @OneToOne
    private User user;

    @ManyToOne
    private School school;

    @ManyToOne
    private Classroom classroom;

    @OneToOne(mappedBy = "child")
    private Parent parent;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Absence> absences;


    public Student(User user, School school, Classroom classroom, Parent parent) {
        this.user = user;
        this.school = school;
        this.classroom = classroom;
        this.parent = parent;
        this.grades = new ArrayList<>();
        this.absences = new ArrayList<>();
    }

    public Student() {
        this.grades = new ArrayList<>();
        this.absences = new ArrayList<>();
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

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }
}
