package com.uni.GradeCenter.model.dto.bindingDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateSubjectBindingDTO {
    @NotBlank(message = "Името на предмета е задължително.")
    @Size(min = 5, max = 20, message = "Името на предмета трябва да е между 5 и 20 символа.")
    private String name;
    @NotNull(message = "Училището е задължително.")
    private String school;

    public CreateSubjectBindingDTO(String name, String school) {
        this.name = name;
        this.school = school;
    }

    public CreateSubjectBindingDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
