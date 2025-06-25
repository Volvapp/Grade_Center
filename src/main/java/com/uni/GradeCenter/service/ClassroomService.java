package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Classroom;

import java.util.List;

public interface ClassroomService {
    Classroom createClassroom(Classroom classroom);

    Classroom getClassroomById(Long id);

    Classroom updateClassroom(Classroom classroom);

    void deleteClassroomById(Long id);

    List<Classroom> getAllClassrooms();
}
