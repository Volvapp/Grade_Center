package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Parent;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.repository.ParentRepository;
import com.uni.GradeCenter.service.ParentService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final UserService userService;
    private final StudentService studentService;

    public ParentServiceImpl(ParentRepository parentRepository, @Lazy UserService userService, StudentService studentService) {
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

    @Transactional
    @Override
    public void updateParentInline(Long parentId, String firstName, String lastName, String email, Long childId) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Невалиден родител с ID: " + parentId));

        // Обнови потребителя
        User user = parent.getUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userService.updateUser(user);

        Student newChild = studentService.getStudentById(childId);
        if (newChild == null) {
            throw new IllegalArgumentException("Невалидно дете с ID: " + childId);
        }

        Student currentChild = parent.getChild();
        if (currentChild != null && !currentChild.getId().equals(childId)) {
            currentChild.setParent(null);
            parent.setChild(null);
        }

        Parent oldParent = newChild.getParent();
        if (oldParent != null && !oldParent.getId().equals(parentId)) {
            oldParent.setChild(null);
        }

        parent.setChild(newChild);
        newChild.setParent(parent);

        parentRepository.save(parent);
    }

    @Override
    public void deleteByUserId(Long id) {
        this.parentRepository.deleteByUser_Id(id);
    }
}
