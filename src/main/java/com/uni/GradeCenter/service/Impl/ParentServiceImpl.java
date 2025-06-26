package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Parent;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.repository.ParentRepository;
import com.uni.GradeCenter.service.ParentService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final UserService userService;
    private final StudentService studentService;

    public ParentServiceImpl(ParentRepository parentRepository, UserService userService, StudentService studentService) {
        this.parentRepository = parentRepository;
        this.userService = userService;
        this.studentService = studentService;
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

    @Override
    public void updateParentInline(Long parentId, String firstName, String lastName, String email, Long childId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Невалиден родител с ID: " + parentId));

        User user = parent.getUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userService.updateUser(user);

        Student child = studentService.getStudentById(childId);

        parent.setChild(child);
        this.updateParent(parent);
    }
}
