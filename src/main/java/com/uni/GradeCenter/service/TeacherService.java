package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Teacher;

public interface TeacherService {
    Teacher createTeacher(Teacher teacher);

    Teacher getTeacherById(Long id);

    Teacher updateTeacher(Teacher teacher);

    void deleteTeacherById(Long id);

    void initializeTeachers();
}
