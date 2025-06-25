package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.repository.SubjectRepository;
import com.uni.GradeCenter.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElseThrow();
    }

    @Override
    public Subject updateSubject(Subject subject) {
        Subject current = subjectRepository.findById(subject.getId()).orElseThrow();
        // Може да актуализираш полетата, ако има такива
        current.setName(subject.getName());
        return subjectRepository.save(current);
    }

    @Override
    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }
}
