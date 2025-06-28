package com.uni.GradeCenter.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "subjects")
public class Subject extends BaseEntity{

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private School school;

    public Subject(String name, School school) {
        this.name = name;
        this.school = school;
    }
    public Subject() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
