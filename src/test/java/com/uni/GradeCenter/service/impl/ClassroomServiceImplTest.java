package com.uni.GradeCenter.service.impl;

import com.uni.GradeCenter.model.Classroom;
import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.ClassroomDTO;
import com.uni.GradeCenter.model.dto.viewDTOs.ClassroomViewDTO;
import com.uni.GradeCenter.repository.ClassroomRepository;
import com.uni.GradeCenter.service.Impl.ClassroomServiceImpl;
import com.uni.GradeCenter.service.SchoolService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceImplTest {

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private SchoolService schoolService;

    @InjectMocks
    private ClassroomServiceImpl classroomService;

    @Test
    void testCreateClassroom() {
        Classroom classroom = new Classroom("6A", 6, new School());
        when(classroomRepository.save(classroom)).thenReturn(classroom);

        Classroom result = classroomService.createClassroom(classroom);

        assertEquals(classroom, result);
        verify(classroomRepository).save(classroom);
    }

    @Test
    void testGetClassroomById_found() {
        Classroom classroom = new Classroom("6A", 6, new School());
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));

        Classroom result = classroomService.getClassroomById(1L);

        assertEquals(classroom, result);
    }

    @Test
    void testGetClassroomById_notFound() {
        when(classroomRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> classroomService.getClassroomById(2L));
    }

    @Test
    void testUpdateClassroom() {
        Classroom classroom = new Classroom("8B", 8, new School());
        when(classroomRepository.save(classroom)).thenReturn(classroom);

        Classroom result = classroomService.updateClassroom(classroom);

        assertEquals(classroom, result);
    }

    @Test
    void testDeleteClassroomById() {
        classroomService.deleteClassroomById(3L);
        verify(classroomRepository).deleteById(3L);
    }

    @Test
    void testGetAllClassrooms() {
        List<Classroom> classrooms = List.of(new Classroom(), new Classroom());
        when(classroomRepository.findAll()).thenReturn(classrooms);

        List<Classroom> result = classroomService.getAllClassrooms();

        assertEquals(2, result.size());
    }

    @Test
    void testGetClassroomsByIds() {
        List<Long> ids = List.of(1L, 2L);
        List<Classroom> classrooms = List.of(new Classroom(), new Classroom());
        when(classroomRepository.findAllById(ids)).thenReturn(classrooms);

        List<Classroom> result = classroomService.getClassroomsByIds(ids);

        assertEquals(2, result.size());
    }

    @Test
    void testFindAll() {
        List<Classroom> classrooms = List.of(new Classroom(), new Classroom());
        when(classroomRepository.findAll()).thenReturn(classrooms);

        Collection<Classroom> result = classroomService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void testFindClassroomsBySchoolId() {
        School school = new School();
        List<Classroom> classrooms = List.of(new Classroom(), new Classroom());
        school.setClassrooms(classrooms);
        when(schoolService.getSchoolById(1L)).thenReturn(school);

        List<Classroom> result = classroomService.findClassroomsBySchoolId(1L);

        assertEquals(classrooms, result);
    }

    @Test
    void testGetAllClassroomViewDTOs() {
        School school = new School();
        User director = new User();
        director.setFirstName("John");
        director.setLastName("Doe");
        school.setName("Test School");
        school.setDirector(director);

        Classroom classroom = new Classroom("7A", 7, school);
        classroom.setId(1L);

        when(classroomRepository.findAll()).thenReturn(List.of(classroom));

        List<ClassroomViewDTO> result = classroomService.getAllClassroomViewDTOs();

        assertEquals(1, result.size());
        assertEquals("7A", result.get(0).getClassroomName());
        assertEquals("Test School", result.get(0).getSchoolName());
        assertEquals("John Doe", result.get(0).getSchoolDirectorName());
    }

    @Test
    void testCheckAvailability_returnsTrue() {
        ClassroomDTO dto = new ClassroomDTO();
        dto.setName("6A");

        Classroom classroom = new Classroom("6A", 6, new School());
        School school = new School();
        school.setClassrooms(List.of(classroom));

        boolean result = classroomService.checkAvailability(dto, school);

        assertTrue(result);
    }

    @Test
    void testCheckAvailability_returnsFalse() {
        ClassroomDTO dto = new ClassroomDTO();
        dto.setName("9Z");

        Classroom classroom = new Classroom("6A", 6, new School());
        School school = new School();
        school.setClassrooms(List.of(classroom));

        boolean result = classroomService.checkAvailability(dto, school);

        assertFalse(result);
    }
}
