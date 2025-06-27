package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.repository.SubjectRepository;
import com.uni.GradeCenter.service.SchoolService;
import com.uni.GradeCenter.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    private final SchoolService schoolService;

    public SubjectServiceImpl(SubjectRepository subjectRepository, SchoolService schoolService) {
        this.subjectRepository = subjectRepository;
        this.schoolService = schoolService;
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

        Optional<School> schoolOpt = schoolService.findBySchoolName("First Language School");
        Optional<School> schoolOpt2 = schoolService.findBySchoolName("Second Language School");
        Optional<School> schoolOpt3 = schoolService.findBySchoolName("Third Language School");
        Optional<School> schoolOpt4 = schoolService.findBySchoolName("Fourth Language School");

        if (schoolOpt.isEmpty()) {
            throw new IllegalStateException("No schools found! Cannot create subjects without a school.");
        }

        School school = schoolOpt.get();

        Subject math = new Subject("Mathematics", school);
        Subject history = new Subject("History", school);
        Subject biology = new Subject("Biology", school);
        Subject english = new Subject("English", school);

        Subject math2 = new Subject("Mathematics", schoolOpt2.get());
        Subject history2 = new Subject("History", schoolOpt2.get());
        Subject biology2 = new Subject("Biology", schoolOpt2.get());
        Subject english2 = new Subject("English", schoolOpt2.get());

        Subject math3 = new Subject("Mathematics", schoolOpt3.get());
        Subject history3 = new Subject("History", schoolOpt3.get());
        Subject biology3 = new Subject("Biology", schoolOpt3.get());
        Subject english3 = new Subject("English", schoolOpt3.get());

        Subject math4 = new Subject("Mathematics", schoolOpt4.get());
        Subject history4 = new Subject("History", schoolOpt4.get());
        Subject biology4 = new Subject("Biology", schoolOpt4.get());
        Subject english4 = new Subject("English", schoolOpt4.get());

        subjectRepository.saveAll(List.of(math, history, biology, english, math2, history2, biology2, english2, math3, history3, biology3, english3, math4, history4, biology4, english4));
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }
}
