package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.repository.SubjectRepository;
import com.uni.GradeCenter.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final SchoolRepository schoolRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, SchoolRepository schoolRepository) {
        this.subjectRepository = subjectRepository;
        this.schoolRepository = schoolRepository;
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

    @Override
    public List<Subject> getAllSubjectsByIds(List<Long> ids) {
        return this.subjectRepository.findAllById(ids);
    }

    @Override
    public void initializeSubjects() {
        if (subjectRepository.count() > 0) return;

        Optional<School> schoolOpt = schoolRepository.findAll().stream().findFirst();

        if (schoolOpt.isEmpty()) {
            throw new IllegalStateException("No schools found! Cannot create subjects without a school.");
        }

        School school = schoolOpt.get();

        Subject math = new Subject("Mathematics", school);
        Subject history = new Subject("History", school);
        Subject biology = new Subject("Biology", school);
        Subject english = new Subject("English", school);

        subjectRepository.saveAll(List.of(math, history, biology, english));
    }
}
