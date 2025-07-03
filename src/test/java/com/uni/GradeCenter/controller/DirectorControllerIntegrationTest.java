package com.uni.GradeCenter.controller;



import com.uni.GradeCenter.model.Parent;
import com.uni.GradeCenter.model.School;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.service.GradeService;
import com.uni.GradeCenter.service.SchoolService;
import com.uni.GradeCenter.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.uni.GradeCenter.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DirectorControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private SchoolService schoolService;

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private DirectorController directorController;

    private User testUser;
    private School testSchool;
    private Teacher testTeacher;
    private Student testStudent;
    private Parent testParent;
    private Subject testSubject;
    private Classroom testClassroom;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(directorController).build();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("director1");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        testSchool = new School();
        testSchool.setId(1L);
        testSchool.setName("Test School");

        testTeacher = new Teacher();
        testTeacher.setId(1L);
        User teacherUser = new User();
        teacherUser.setFirstName("Jane");
        teacherUser.setLastName("Smith");
        testTeacher.setUser(teacherUser);

        testParent = new Parent();
        testParent.setId(1L);
        User parentUser = new User();
        parentUser.setFirstName("Parent");
        parentUser.setLastName("One");
        testParent.setUser(parentUser);

        testStudent = new Student();
        testStudent.setId(1L);
        User studentUser = new User();
        studentUser.setFirstName("Student");
        studentUser.setLastName("One");
        testStudent.setUser(studentUser);
        testStudent.setParent(testParent);

        testSubject = new Subject();
        testSubject.setId(1L);
        testSubject.setName("Mathematics");

        testClassroom = new Classroom();
        testClassroom.setId(1L);
        testClassroom.setName("Class 10A");

        testSchool.setTeachers(List.of(testTeacher));
        testSchool.setStudents(List.of(testStudent));
        testSchool.setSubjects(List.of(testSubject));
        testSchool.setClassrooms(List.of(testClassroom));
    }

    @Test
    void testSchoolInformationEndpoint() throws Exception {
        when(userService.findByUsername("director1")).thenReturn(testUser);
        when(schoolService.findByUserId(1L)).thenReturn(testSchool);
        when(gradeService.countGradesPerSubjectForSchool(1L)).thenReturn(Map.of("Mathematics", 10L));
        when(gradeService.averageGradesPerTeacherForSchool(1L)).thenReturn(Map.of("Jane Smith", 4.5));
        when(gradeService.calculateOverallAverageForSchool(1L)).thenReturn(4.5);

        mockMvc.perform(get("/director/school")
                        .principal(() -> "director1"))
                .andExpect(status().isOk())
                .andExpect(view().name("director-school"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("school"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("subjects"))
                .andExpect(model().attributeExists("classrooms"))
                .andExpect(model().attributeExists("parents"))
                .andExpect(model().attributeExists("gradesPerSubject"))
                .andExpect(model().attributeExists("averagePerTeacher"))
                .andExpect(model().attributeExists("overallAverage"))

                .andExpect(model().attribute("user", hasProperty("username", is("director1"))))
                .andExpect(model().attribute("school", hasProperty("name", is("Test School"))))
                .andExpect(model().attribute("teachers", hasSize(1)))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("subjects", hasSize(1)))
                .andExpect(model().attribute("classrooms", hasSize(1)))
                .andExpect(model().attribute("parents", hasSize(1)))
                .andExpect(model().attribute("gradesPerSubject", hasEntry("Mathematics", 10L)))
                .andExpect(model().attribute("averagePerTeacher", hasEntry("Jane Smith", 4.5)))
                .andExpect(model().attribute("overallAverage", is(4.5)));

        verify(userService).findByUsername("director1");
        verify(schoolService).findByUserId(1L);
        verify(gradeService).countGradesPerSubjectForSchool(1L);
        verify(gradeService).averageGradesPerTeacherForSchool(1L);
        verify(gradeService).calculateOverallAverageForSchool(1L);
    }

    @Test
    void testSchoolInformationEndpointWithNullParent() throws Exception {
        testStudent.setParent(null);
        testSchool.setStudents(List.of(testStudent));

        when(userService.findByUsername("director1")).thenReturn(testUser);
        when(schoolService.findByUserId(1L)).thenReturn(testSchool);
        when(gradeService.countGradesPerSubjectForSchool(1L)).thenReturn(Map.of());
        when(gradeService.averageGradesPerTeacherForSchool(1L)).thenReturn(Map.of());
        when(gradeService.calculateOverallAverageForSchool(1L)).thenReturn(0.0);

        mockMvc.perform(get("/director/school")
                        .principal(() -> "director1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("parents", hasSize(0)));

        verify(userService).findByUsername("director1");
        verify(schoolService).findByUserId(1L);
    }
}

