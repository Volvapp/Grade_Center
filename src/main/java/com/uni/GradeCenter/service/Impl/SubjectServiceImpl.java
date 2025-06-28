package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Subject;
import com.uni.GradeCenter.repository.SchoolRepository;
import com.uni.GradeCenter.repository.SubjectRepository;
import com.uni.GradeCenter.service.SchoolService;
import com.uni.GradeCenter.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        List<SchoolSubjectData> schoolSubjectDataList = List.of(
                new SchoolSubjectData("First Language School", List.of("Mathematics", "History", "Biology", "English")),
                new SchoolSubjectData("Second Language School", List.of("Physics", "Chemistry", "Philosophy", "English Literature")),
                new SchoolSubjectData("Third Language School", List.of("Geography", "Informatics", "Art", "Physical Education")),
                new SchoolSubjectData("Fourth Language School", List.of("Music", "Economics", "Programming", "Statistics"))
        );

        List<Subject> allSubjects = new ArrayList<>();

        for (SchoolSubjectData schoolData : schoolSubjectDataList) {
            School school = schoolService.findBySchoolName(schoolData.schoolName())
                    .orElseThrow(() -> new IllegalStateException("School not found: " + schoolData.schoolName()));

            for (String subjectName : schoolData.subjectNames()) {
                allSubjects.add(new Subject(subjectName, school));
            }
        }

        subjectRepository.saveAll(allSubjects);
    }

    private record SchoolSubjectData(String schoolName, List<String> subjectNames) {}


    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Override
    public List<Subject> getAllSubjectsBySchoolId(Long schoolId) {
        return subjectRepository.findBySchool_Id(schoolId);
    }

}
