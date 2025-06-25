package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Absence;
import com.uni.GradeCenter.repository.AbsenceRepository;
import com.uni.GradeCenter.service.AbsenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceRepository absenceRepository;

    public AbsenceServiceImpl(AbsenceRepository absenceRepository) {
        this.absenceRepository = absenceRepository;
    }

    @Override
    public Absence createAbsence(Absence absence) {
        return absenceRepository.save(absence);
    }

    @Override
    public Absence getAbsenceById(Long id) {
        return absenceRepository.findById(id).orElseThrow();
    }

    @Override
    public Absence updateAbsence(Absence absence) {
        Absence existingAbsence = absenceRepository.findById(absence.getId()).orElseThrow();
        return absenceRepository.save(existingAbsence);
    }

    @Override
    public void deleteAbsenceById(Long id) {
        absenceRepository.deleteById(id);
    }

    @Override
    public List<Absence> getAllAbsences() {
        return absenceRepository.findAll();
    }
}
