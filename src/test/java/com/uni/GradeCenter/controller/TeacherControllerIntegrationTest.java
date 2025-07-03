package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.bindingDTOs.AbsenceCreateBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.CreateGradeBindingDTO;
import com.uni.GradeCenter.model.dto.bindingDTOs.EditGradeBindingDTO;
import com.uni.GradeCenter.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "teacher1", roles = {"TEACHER"})
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private GradeService gradeService;

    @Mock
    private AbsenceService absenceService;

    @Mock
    private UserService userService;

    private Teacher testTeacher;
    private Subject testSubject;
    private Classroom testClassroom;
    private Student testStudent;
    private Schedule testSchedule;
    private Grade testGrade;
    private Absence testAbsence;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("teacher1");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setUser(testUser);

        testSubject = new Subject();
        testSubject.setId(1L);
        testSubject.setName("Mathematics");

        testClassroom = new Classroom();
        testClassroom.setId(1L);
        testClassroom.setName("Class A");
        School school = new School();
        school.setName("Test School");
        testClassroom.setSchool(school);

        testStudent = new Student();
        testStudent.setId(1L);
        User studentUser = new User();
        studentUser.setFirstName("Alice");
        studentUser.setLastName("Smith");
        testStudent.setUser(studentUser);

        testClassroom.setStudents(Collections.singletonList(testStudent));

        testSchedule = new Schedule();
        testSchedule.setId(1L);
        testSchedule.setTeacher(testTeacher);
        testSchedule.setSubject(testSubject);
        testSchedule.setClassroom(testClassroom);

        testGrade = new Grade();
        testGrade.setId(1L);
        testGrade.setValue(5.5);
        testGrade.setStudent(testStudent);
        testGrade.setSubject(testSubject);

        testAbsence = new Absence();
        testAbsence.setId(1L);
        testAbsence.setStudent(testStudent);
        testAbsence.setSubject(testSubject);
        testAbsence.setDate(LocalDate.now());

        testTeacher.setSchedules(Collections.singletonList(testSchedule));
        testTeacher.setQualifiedSubjects(Collections.singletonList(testSubject));

        Mockito.when(userService.findByUsername("teacher1")).thenReturn(testUser);
        Mockito.when(teacherService.findByUsername("teacher1")).thenReturn(testTeacher);
        Mockito.when(teacherService.getTeacherByUserId(1L)).thenReturn(testTeacher);
        Mockito.when(scheduleService.findByTeacherAndSubject(testTeacher, testSubject))
                .thenReturn(Collections.singletonList(testSchedule));
        Mockito.when(gradeService.findByStudentAndSubject(testStudent, testSubject))
                .thenReturn(Collections.singletonList(testGrade));
        Mockito.when(absenceService.findByStudentAndSubject(testStudent, testSubject))
                .thenReturn(testAbsence);
        Mockito.when(gradeService.getGradeById(1L)).thenReturn(testGrade);
    }

    @Test
    void testShowGradeForm() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("classroomId", "1");
        params.add("classroomName", "Class A");
        params.add("studentId", "1");
        params.add("studentName", "Alice Smith");
        params.add("subjectId", "1");
        params.add("subjectName", "Mathematics");

        mockMvc.perform(MockMvcRequestBuilders.get("/teacher/grade/create")
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher-grade-create"))
                .andExpect(model().attributeExists("createGradeBindingDTO"))
                .andExpect(model().attribute("createGradeBindingDTO",
                        allOf(
                                hasProperty("classroomId", is(1L)),
                                hasProperty("classroomName", is("Class A")),
                                hasProperty("studentId", is(1L)),
                                hasProperty("studentName", is("Alice Smith")),
                                hasProperty("subjectId", is(1L)),
                                hasProperty("subjectName", is("Mathematics"))
                        )));
    }

    @Test
    void testSubmitGradeWithValidationErrors() throws Exception {
        CreateGradeBindingDTO dto = new CreateGradeBindingDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/teacher/grade/create")
                        .flashAttr("createGradeBindingDTO", dto)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/teacher/grade/create*"));
    }

    @Test
    void testSubmitAbsenceWithValidationErrors() throws Exception {
        AbsenceCreateBindingDTO dto = new AbsenceCreateBindingDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/teacher/absence/create")
                        .flashAttr("createAbsenceBindingDTO", dto)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/teacher/absence/create*"));
    }
}
