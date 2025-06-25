package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Parent;

import java.util.List;

public interface ParentService {
    Parent createParent(Parent parent);

    Parent getParentById(Long id);

    Parent updateParent(Parent parent);

    void deleteParentById(Long id);

    List<Parent> getAllParents();
}
