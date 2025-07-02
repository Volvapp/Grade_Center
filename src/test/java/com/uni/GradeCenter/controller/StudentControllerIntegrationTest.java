package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.viewDTOs.ScheduleViewDTO;
import com.uni.GradeCenter.service.ScheduleService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.service.ScheduleService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.service.ScheduleService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private StudentService studentService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private StudentController studentController;

    private User testUser;
    private Student testStudent;
    private Classroom testClassroom;
    private Schedule testSchedule;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();

        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("student1");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        // Setup test classroom
        testClassroom = new Classroom();
        testClassroom.setId(1L);
        testClassroom.setName("Class 10A");

        // Setup test student
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setUser(testUser);
        testStudent.setClassroom(testClassroom);

        // Setup test schedule
        testSchedule = new Schedule();
        testSchedule.setId(1L);
        Subject subject = new Subject();
        subject.setName("Mathematics");
        testSchedule.setSubject(subject);
        Teacher teacher = new Teacher();
        User teacherUser = new User();
        teacherUser.setFirstName("Jane");
        teacherUser.setLastName("Smith");
        teacher.setUser(teacherUser);
        testSchedule.setTeacher(teacher);

        testClassroom.setSchedules(Collections.singletonList(testSchedule));

        // Setup security context with mock principal
        Authentication authentication = new UsernamePasswordAuthenticationToken("student1", "password");
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testStatisticsEndpoint() throws Exception {
        when(userService.findByUsername("student1")).thenReturn(testUser);
        when(studentService.getStudentByUserId(1L)).thenReturn(testStudent);

        mockMvc.perform(get("/student/statistics")
                        .principal(() -> "student1")) // Add mock principal to request
                .andExpect(status().isOk())
                .andExpect(view().name("student-statistics"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("student",
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("user",
                                        allOf(
                                                hasProperty("firstName", is("John")),
                                                hasProperty("lastName", is("Doe"))
                                        )
                                ))));

        verify(userService).findByUsername("student1");
        verify(studentService).getStudentByUserId(1L);
    }

    @Test
    void testScheduleEndpoint() throws Exception {
        when(userService.findByUsername("student1")).thenReturn(testUser);
        when(studentService.getStudentByUserId(1L)).thenReturn(testStudent);
        when(scheduleService.mapToView(anyList())).thenReturn(Collections.singletonList(new ScheduleViewDTO()));

        mockMvc.perform(get("/student/schedule")
                        .principal(() -> "student1")) // Add mock principal to request
                .andExpect(status().isOk())
                .andExpect(view().name("student-schedule"))
                .andExpect(model().attributeExists("schedules"))
                .andExpect(model().attribute("schedules", hasSize(1)));

        verify(userService).findByUsername("student1");
        verify(studentService).getStudentByUserId(1L);
        verify(scheduleService).mapToView(anyList());
    }
}
