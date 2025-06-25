package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Parent;
import com.uni.GradeCenter.repository.ParentRepository;
import com.uni.GradeCenter.service.ParentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;

    public ParentServiceImpl(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    @Override
    public Parent createParent(Parent parent) {
        return parentRepository.save(parent);
    }

    @Override
    public Parent getParentById(Long id) {
        return parentRepository.findById(id).orElseThrow();
    }

    @Override
    public Parent updateParent(Parent parent) {
        Parent existing = parentRepository.findById(parent.getId()).orElseThrow();
        return parentRepository.save(existing);
    }

    @Override
    public void deleteParentById(Long id) {
        parentRepository.deleteById(id);
    }

    @Override
    public List<Parent> getAllParents() {
        return parentRepository.findAll();
    }
}
