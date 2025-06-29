package com.uni.GradeCenter.model.dto.viewDTOs;

public class SubjectViewDTO {
    private String name;
    private double averageGrade;
    private int totalAbsences;

    public SubjectViewDTO(String name, double averageGrade, int totalAbsences) {
        this.name = name;
        this.averageGrade = averageGrade;
        this.totalAbsences = totalAbsences;
    }

    public SubjectViewDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(double averageGrade) {
        this.averageGrade = averageGrade;
    }

    public int getTotalAbsences() {
        return totalAbsences;
    }

    public void setTotalAbsences(int totalAbsences) {
        this.totalAbsences = totalAbsences;
    }
}
