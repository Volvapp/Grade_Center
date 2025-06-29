package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Absence;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.model.dto.bindingDTOs.AbsenceCreateBindingDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AbsenceService {

    Absence createAbsence(Absence absence);

    Absence getAbsenceById(Long id);

    Absence updateAbsence(Absence absence);

    void deleteAbsenceById(Long id);

    List<Absence> getAllAbsences();

    void initializeAbsences();

    Absence findByStudentAndSubject(Student student, Subject subject);

    void createAbsenceFrontend(@Valid AbsenceCreateBindingDTO dto, String name);
}
