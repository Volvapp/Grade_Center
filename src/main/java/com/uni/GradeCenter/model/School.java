package com.uni.GradeCenter.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "schools")
public class School extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne
    private User director;

    @OneToMany(mappedBy = "school")
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "school")
    private List<Student> students;

    @OneToMany(mappedBy = "school")
    private List<Subject> subjects;

    @OneToMany(mappedBy = "school")
    private List<Classroom> classrooms;

    public School(String name, String address, User director, List<Teacher> teachers, List<Student> students, List<Subject> subjects, List<Classroom> classrooms) {
        this.name = name;
        this.address = address;
        this.director = director;
        this.teachers = teachers;
        this.students = students;
        this.subjects = subjects;
        this.classrooms = classrooms;
    }

    public School() {
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

    public User getDirector() {
        return director;
    }

    public void setDirector(User director) {
        this.director = director;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
