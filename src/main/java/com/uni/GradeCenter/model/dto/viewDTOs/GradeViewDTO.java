package com.uni.GradeCenter.model.dto.viewDTOs;

public class GradeViewDTO {
    private Long id;
    private Double value;

    public GradeViewDTO(Long id, Double value) {
        this.id = id;
        this.value = value;
    }

    public GradeViewDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
