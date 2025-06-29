package com.uni.GradeCenter.model.dto.bindingDTOs;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

public class EditGradeBindingDTO {
    private Long gradeId;

    @DecimalMin("2.00")
    @DecimalMax("6.00")
    @Digits(integer = 1, fraction = 2)
    private Double value;

    public EditGradeBindingDTO(Long gradeId, Double value) {
        this.gradeId = gradeId;
        this.value = value;
    }

    public EditGradeBindingDTO() {
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
