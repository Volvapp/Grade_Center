package com.uni.GradeCenter.model.dto.bindingDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateSchoolBindingDTO {
    @NotBlank(message = "Името на училището е задължително.")
    @Size(min = 5, max = 20, message = "Името на училището трябва да е между 5 и 20 символа.")
    private String name;
    @NotBlank(message = "Адресът е задължителен.")
    @Size(min = 5, max = 20, message = "Адресът трябва да е между 5 и 20 символа.")
    private String address;
    @NotNull(message = "Директорът е задължителен.")
    private String director;

    public CreateSchoolBindingDTO() {
    }

    public CreateSchoolBindingDTO(String name, String address, String director) {
        this.name = name;
        this.address = address;
        this.director = director;
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

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
