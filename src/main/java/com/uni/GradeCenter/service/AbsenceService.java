package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Absence;

import java.util.List;

public interface AbsenceService {

    Absence createAbsence(Absence absence);

    Absence getAbsenceById(Long id);

    Absence updateAbsence(Absence absence);

    void deleteAbsenceById(Long id);

    List<Absence> getAllAbsences();
}
