package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher createTeacher(Teacher teacher);

    Teacher getTeacherById(Long id);

    Teacher updateTeacher(Teacher teacher);

    void deleteTeacherById(Long id);

    void initializeTeachers();

    List<Teacher> getAllTeachers();

    List<Teacher> getTeachersByIds(List<Long> teacherIds);

    void deleteByUserId(Long id);

    Teacher getTeacherByUserId(Long id);

    Teacher findByUsername(String name);
}
