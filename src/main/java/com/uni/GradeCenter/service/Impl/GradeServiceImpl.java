package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Grade;
import com.uni.GradeCenter.repository.GradeRepository;
import com.uni.GradeCenter.service.GradeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;

    public GradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public Grade createGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id).orElseThrow();
    }

    @Override
    public Grade updateGrade(Grade grade) {
        Grade existing = gradeRepository.findById(grade.getId()).orElseThrow();
        return gradeRepository.save(existing);
    }

    @Override
    public void deleteGradeById(Long id) {
        gradeRepository.deleteById(id);
    }

    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }
}
