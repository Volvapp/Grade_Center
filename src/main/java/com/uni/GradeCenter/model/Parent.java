package com.uni.GradeCenter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "parents")
public class Parent extends BaseEntity{
    @OneToOne
    private User user;

    @OneToOne
    private Student child;

    public Parent(User user, Student child) {
        this.user = user;
        this.child = child;
    }
    public Parent() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Student getChild() {
        return child;
    }

    public void setChild(Student child) {
        this.child = child;
    }
}
