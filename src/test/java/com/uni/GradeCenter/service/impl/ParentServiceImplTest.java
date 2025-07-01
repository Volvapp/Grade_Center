package com.uni.GradeCenter.service.impl;

import com.uni.GradeCenter.model.Parent;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.repository.ParentRepository;
import com.uni.GradeCenter.service.Impl.ParentServiceImpl;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParentServiceImplTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private UserService userService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private ParentServiceImpl parentService;

    private Parent parent;
    private User user;
    private Student student;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        student = new Student();
        student.setId(10L);

        parent = new Parent(user, student);
        parent.setId(5L);
    }

    @Test
    void testCreateParent_success() {
        when(parentRepository.save(any(Parent.class))).thenReturn(parent);

        Parent saved = parentService.createParent(parent);

        assertNotNull(saved);
        verify(parentRepository).save(parent);
    }

    @Test
    void testGetParentById_found() {
        when(parentRepository.findById(5L)).thenReturn(Optional.of(parent));

        Parent result = parentService.getParentById(5L);

        assertEquals(5L, result.getId());
    }

    @Test
    void testGetParentById_notFound_throwsException() {
        when(parentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> parentService.getParentById(99L));
    }

    @Test
    void testGetParentByUserId_success() {
        when(parentRepository.findByUser_Id(1L)).thenReturn(parent);

        Parent result = parentService.getParentByUserId(1L);

        assertEquals(1L, result.getUser().getId());
    }

    @Test
    void testUpdateParent_success() {
        when(parentRepository.save(parent)).thenReturn(parent);

        Parent updated = parentService.updateParent(parent);

        assertEquals(parent, updated);
    }

    @Test
    void testDeleteParentById_success() {
        parentService.deleteParentById(5L);

        verify(parentRepository).deleteById(5L);
    }

    @Test
    void testGetAllParents() {
        when(parentRepository.findAll()).thenReturn(List.of(parent));

        List<Parent> result = parentService.getAllParents();

        assertEquals(1, result.size());
        assertEquals(parent, result.get(0));
    }

    @Test
    void testDeleteByUserId_success() {
        parentService.deleteByUserId(1L);

        verify(parentRepository).deleteByUser_Id(1L);
    }

    @Test
    void testUpdateParentInline_changeUserDataAndUnlinkChild() {
        parent.setChild(student);
        student.setParent(parent);

        when(parentRepository.findById(5L)).thenReturn(Optional.of(parent));

        parentService.updateParentInline(5L, "Jane", "Smith", "jane@example.com", null);

        assertEquals("Jane", user.getFirstName());
        assertNull(parent.getChild());
        assertNull(student.getParent());
        verify(userService).updateUser(user);
        verify(parentRepository).save(parent);
    }

    @Test
    void testUpdateParentInline_assignNewChild() {
        Student newStudent = new Student();
        newStudent.setId(20L);

        Parent oldParent = new Parent();
        oldParent.setId(7L);
        newStudent.setParent(oldParent);

        when(parentRepository.findById(5L)).thenReturn(Optional.of(parent));
        when(studentService.getStudentById(20L)).thenReturn(newStudent);

        parentService.updateParentInline(5L, "Jane", "Smith", "jane@example.com", 20L);

        assertEquals(parent, newStudent.getParent());
        assertEquals(newStudent, parent.getChild());
        assertNull(oldParent.getChild());
        verify(userService).updateUser(user);
        verify(parentRepository).save(parent);
    }

    @Test
    void testUpdateParentInline_invalidParent_throwsException() {
        when(parentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                parentService.updateParentInline(999L, "Test", "User", "test@example.com", null));
    }

    @Test
    void testUpdateParentInline_invalidStudentId_throwsException() {
        when(parentRepository.findById(5L)).thenReturn(Optional.of(parent));
        when(studentService.getStudentById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                parentService.updateParentInline(5L, "Test", "User", "test@example.com", 999L));
    }
}


