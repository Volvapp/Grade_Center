package com.uni.GradeCenter.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classrooms")
public class Classroom extends BaseEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @ManyToOne
    private School school;

    @OneToMany(mappedBy = "classroom")
    private List<Student> students;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Schedule> schedules;

    public Classroom(String name, Integer grade, School school) {
        this.name = name;
        this.grade = grade;
        this.school = school;
        this.students = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }

    public Classroom() {}

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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
