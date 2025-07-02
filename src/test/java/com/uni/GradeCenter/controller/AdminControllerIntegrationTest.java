package com.uni.GradeCenter.controller;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.*;
import com.uni.GradeCenter.model.dto.bindingDTOs.*;
import com.uni.GradeCenter.model.dto.viewDTOs.*;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerIntegrationTest {

    private MockMvc mockMvc;

    @Mock private UserService userService;
    @Mock private SchoolService schoolService;
    @Mock private StudentService studentService;
    @Mock private ParentService parentService;
    @Mock private ClassroomService classroomService;
    @Mock private TeacherService teacherService;
    @Mock private ScheduleService scheduleService;
    @Mock private SubjectService subjectService;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void testUsersEndpoint() throws Exception {
        List<User> mockUsers = List.of(new User(), new User());
        when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"))
                .andExpect(model().attribute("users", hasSize(2)));

        verify(userService).getAllUsers();
    }

    @Test
    void testEditUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");

        mockMvc.perform(post("/admin/users/edit/1")
                        .flashAttr("userDTO", userDTO)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(userService).updateUserFromDTO(1L, userDTO);
    }

    @Test
    void testEditSchool() throws Exception {
        School mockSchool = new School();
        when(schoolService.getSchoolById(1L)).thenReturn(mockSchool);
        when(userService.getUserById(2L)).thenReturn(new User());

        mockMvc.perform(post("/admin/schools/edit/1")
                        .param("name", "New School")
                        .param("address", "New Address")
                        .param("directorId", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/schools"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(schoolService).updateSchool(any(School.class));
    }

    @Test
    void testCreateSchool() throws Exception {
        CreateSchoolBindingDTO dto = new CreateSchoolBindingDTO();
        dto.setName("Test School");
        dto.setAddress("Test Address");
        dto.setDirector("1");

        when(userService.getUserById(1L)).thenReturn(new User());

        mockMvc.perform(post("/admin/schools/create")
                        .flashAttr("createSchoolBindingDTO", dto)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/schools"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(schoolService).saveSchool(any(School.class));
    }

    @Test
    void testStudentsEndpoint() throws Exception {
        List<User> mockStudentUsers = List.of(new User());
        List<Student> mockStudents = List.of(new Student());
        List<School> mockSchools = List.of(new School());
        List<Classroom> mockClassrooms = List.of(new Classroom());

        when(userService.getUsersByRole(Role.STUDENT)).thenReturn(mockStudentUsers);
        when(studentService.getStudentsByUserIds(any())).thenReturn(mockStudents);
        when(schoolService.getAllSchools()).thenReturn(mockSchools);
        when(classroomService.getAllClassrooms()).thenReturn(mockClassrooms);

        mockMvc.perform(get("/admin/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-students"))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("schools", hasSize(1)))
                .andExpect(model().attribute("classrooms", hasSize(1)));
    }

    @Test
    void testEditStudent() throws Exception {
        mockMvc.perform(post("/admin/students/edit/1")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com")
                        .param("username", "johndoe")
                        .param("schoolId", "1")
                        .param("classroomId", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/students"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(studentService).updateStudentInline(anyLong(), anyString(), anyString(),
                anyString(), anyString(), anyLong(), any());
    }

    @Test
    void testParentsEndpoint() throws Exception {
        List<Student> mockStudents = List.of(new Student());
        List<Parent> mockParents = List.of(new Parent());

        when(studentService.getAllStudents()).thenReturn(mockStudents);
        when(parentService.getAllParents()).thenReturn(mockParents);

        mockMvc.perform(get("/admin/parents"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-parents"))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("parents", hasSize(1)));
    }

    @Test
    void testUpdateParent() throws Exception {
        mockMvc.perform(post("/admin/parents/edit/1")
                        .param("firstName", "Parent")
                        .param("lastName", "One")
                        .param("email", "parent@example.com")
                        .param("childId", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/parents"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(parentService).updateParentInline(anyLong(), anyString(), anyString(),
                anyString(), any());
    }

    @Test
    void testTeachersEndpoint() throws Exception {
        List<Teacher> mockTeachers = List.of(new Teacher());
        when(teacherService.getAllTeachers()).thenReturn(mockTeachers);

        mockMvc.perform(get("/admin/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-teachers"))
                .andExpect(model().attribute("teachers", hasSize(1)));
    }

    @Test
    void testCreateClassroomForm() throws Exception {
        List<School> mockSchools = List.of(new School());
        when(schoolService.getAllSchools()).thenReturn(mockSchools);

        mockMvc.perform(get("/admin/schools/classrooms/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-schools-classroom-create"))
                .andExpect(model().attributeExists("classroom"))
                .andExpect(model().attribute("schools", hasSize(1)));
    }

    @Test
    void testCreateSubjectForm() throws Exception {
        List<School> mockSchools = List.of(new School());
        when(schoolService.getAllSchools()).thenReturn(mockSchools);

        mockMvc.perform(get("/admin/subjects/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-subject-create"))
                .andExpect(model().attributeExists("createSubjectBindingDTO"))
                .andExpect(model().attribute("schools", hasSize(1)));
    }



    @Test
    void testSchedulesEndpoint() throws Exception {
        List<ClassroomViewDTO> mockClassrooms = List.of(new ClassroomViewDTO());
        when(classroomService.getAllClassroomViewDTOs()).thenReturn(mockClassrooms);

        mockMvc.perform(get("/admin/schedules"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-create-schedules"))
                .andExpect(model().attribute("classroomViews", hasSize(1)));
    }

    @Test
    void testGetAvailableStartTimes() throws Exception {
        Classroom mockClassroom = new Classroom();
        Subject mockSubject = new Subject();
        AvailableSlotDTO mockSlot = new AvailableSlotDTO(LocalTime.of(9, 0), "Teacher Name");

        when(classroomService.getClassroomById(1L)).thenReturn(mockClassroom);
        when(subjectService.getSubjectById(1L)).thenReturn(mockSubject);
        when(scheduleService.getAvailableStartTimesWithTeachers(any(), any(), any()))
                .thenReturn(List.of(mockSlot));

        mockMvc.perform(get("/admin/schedules/available-times")
                        .param("classroomId", "1")
                        .param("subjectId", "1")
                        .param("dayOfWeek", "MONDAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].time", is("09:00")))
                .andExpect(jsonPath("$[0].teacher", is("Teacher Name")));
    }

    @Test
    void testSchedulesCreate() throws Exception {
        Classroom mockClassroom = new Classroom();
        School mockSchool = new School();
        mockClassroom.setSchool(mockSchool);
        List<Subject> mockSubjects = List.of(new Subject());
        List<LocalTime> mockTimes = List.of(LocalTime.of(9, 0));

        when(classroomService.getClassroomById(1L)).thenReturn(mockClassroom);
        when(subjectService.getSubjectsBySchool(mockSchool)).thenReturn(mockSubjects);
        when(scheduleService.getDailyLessonSlots()).thenReturn(mockTimes);

        mockMvc.perform(get("/admin/schedules/create")
                        .param("classroomId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-create-schedule"))
                .andExpect(model().attributeExists("classroom"))
                .andExpect(model().attribute("subjects", hasSize(1)))
                .andExpect(model().attribute("availableStartTimes", hasSize(1)))
                .andExpect(model().attributeExists("createScheduleBindingDTO"));
    }

    @Test
    void testCreateSchedule() throws Exception {
        CreateScheduleBindingDTO dto = new CreateScheduleBindingDTO();
        dto.setClassroomId(1L);

        mockMvc.perform(post("/admin/schedules/create")
                        .flashAttr("createScheduleBindingDTO", dto)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/schedules"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(scheduleService).createScheduleFrontend(any());
    }

    @Test
    void testStatisticsEndpoint() throws Exception {
        List<School> mockSchools = List.of(new School());
        Map<Long, List<SubjectViewDTO>> mockStats = new HashMap<>();
        mockStats.put(1L, List.of(new SubjectViewDTO()));

        when(schoolService.getAllSchools()).thenReturn(mockSchools);
        when(schoolService.getSchoolStatistics()).thenReturn(mockStats);

        mockMvc.perform(get("/admin/statistics"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-statistics"))
                .andExpect(model().attribute("schools", hasSize(1)))
                .andExpect(model().attributeExists("schoolStats"));
    }
}
