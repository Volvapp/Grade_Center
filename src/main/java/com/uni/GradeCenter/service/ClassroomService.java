package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Classroom;

import java.util.Collection;
import java.util.List;

public interface ClassroomService {
    Classroom createClassroom(Classroom classroom);

    Classroom getClassroomById(Long id);

    Classroom updateClassroom(Classroom classroom);

    void deleteClassroomById(Long id);

    List<Classroom> getAllClassrooms();

    void initializeClassrooms();

    List<Classroom> getClassroomsByIds(List<Long> classroomIds);

    Collection<Classroom> findAll();
}
