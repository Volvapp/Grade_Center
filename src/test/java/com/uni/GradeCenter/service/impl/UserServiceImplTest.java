package com.uni.GradeCenter.service.impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.*;
import com.uni.GradeCenter.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private TeacherService teacherService;
    @Mock private StudentService studentService;
    @Mock private ParentService parentService;
    @Mock private AbsenceService absenceService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // CREATE USER
    @Test
    void testCreateUser() {
        User user = new User();
        user.setPassword("plain");

        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.createUser(user);

        assertEquals("encoded", result.getPassword());
        verify(userRepository).save(user);
    }

    // GET USER BY ID
    @Test
    void testGetUserById_Found() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(user, result);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }

    // UPDATE USER
    @Test
    void testUpdateUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(user);

        assertEquals(user, result);
    }

    // FIND BY ROLE
    @Test
    void testFindByRole() {
        User user = new User();
        when(userRepository.findByRole(Role.STUDENT)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByRole(Role.STUDENT);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    // DELETE USER
    @Test
    void testDeleteUserById() {
        userService.deleteUserById(3L);
        verify(userRepository).deleteById(3L);
    }

    // GET ALL USERS
    @Test
    void testGetAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
    }

    // USERNAME OR EMAIL EXISTS
    @Test
    void testUsernameOrEmailExists_True() {
        when(userRepository.findByUsername("u")).thenReturn(new User());
        when(userRepository.findByEmail("e")).thenReturn(null);

        boolean result = userService.usernameOrEmailExists("u", "e");

        assertTrue(result);
    }

    @Test
    void testUsernameOrEmailExists_False() {
        when(userRepository.findByUsername("u")).thenReturn(null);
        when(userRepository.findByEmail("e")).thenReturn(null);

        boolean result = userService.usernameOrEmailExists("u", "e");

        assertFalse(result);
    }

    // INITIALIZE USERS
    @Test
    void testInitializeUsers_SkippedIfAlreadyInitialized() {
        when(userRepository.count()).thenReturn(10L);

        userService.initializeUsers();

        verify(userRepository, never()).saveAll(any());
    }

    @Test
    void testInitializeUsers_SavesNewUsers() {
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode("1234")).thenReturn("encoded");

        userService.initializeUsers();

        verify(userRepository).saveAll(argThat(users -> {
            List<User> userList = new ArrayList<>();
            for (User user : users) {
                userList.add(user);
            }
            return userList.size() == 18;
        }));
    }

    // UPDATE USER FROM DTO
    @Test
    void testUpdateUserFromDTO_ChangeRoleToStudent() {
        Long userId = 1L;
        User user = new User();
        user.setRole(Role.TEACHER); // Old role

        School school = new School();
        school.setTeachers(new ArrayList<>());  // Initialize teachers list

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setSchool(school);
        teacher.setSchedules(new ArrayList<>());
        teacher.setGrades(new ArrayList<>());

        school.getTeachers().add(teacher); // Add teacher to school's teachers list

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(teacherService.getTeacherByUserId(userId)).thenReturn(teacher);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        UserDTO dto = new UserDTO();
        dto.setFirstName("NewFirst");
        dto.setLastName("NewLast");
        dto.setEmail("new@example.com");
        dto.setRole(Role.STUDENT); // New role

        userService.updateUserFromDTO(userId, dto);

        verify(teacherService).updateTeacher(any());
        verify(teacherService).deleteByUserId(userId);
        verify(studentService).createStudent(any(Student.class));
    }

    @Test
    void testUpdateUserFromDTO_ChangeRoleToParent() {
        Long userId = 1L;
        User user = new User();
        user.setRole(Role.STUDENT); // Old role

        Student student = new Student();
        Parent parent = new Parent();
        student.setParent(parent);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(studentService.getStudentByUserId(userId)).thenReturn(student);

        UserDTO dto = new UserDTO();
        dto.setFirstName("F");
        dto.setLastName("L");
        dto.setEmail("e@mail.com");
        dto.setRole(Role.PARENT); // New

        userService.updateUserFromDTO(userId, dto);

        verify(studentService).deleteByUserId(userId);
        verify(parentService).updateParent(parent);
        verify(parentService).createParent(any(Parent.class));
    }

    @Test
    void testUpdateUserFromDTO_SameRole_OnlyUpdateFields() {
        Long userId = 1L;
        User user = new User();
        user.setRole(Role.PARENT);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO dto = new UserDTO();
        dto.setFirstName("A");
        dto.setLastName("B");
        dto.setEmail("email@test.com");
        dto.setRole(Role.PARENT);

        userService.updateUserFromDTO(userId, dto);

        assertEquals("A", user.getFirstName());
        assertEquals("B", user.getLastName());
        assertEquals("email@test.com", user.getEmail());
        verify(userRepository).save(user);
    }

    // FIND USERS BY ROLE
    @Test
    void testGetUsersByRole() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAllByRole(Role.STUDENT)).thenReturn(users);

        List<User> result = userService.getUsersByRole(Role.STUDENT);

        assertEquals(2, result.size());
    }

    // FIND BY ROLE AND USERNAME
    @Test
    void testFindByRoleAndUsername() {
        Optional<User> user = Optional.of(new User());
        when(userRepository.findByRoleAndUsername(Role.DIRECTOR, "john")).thenReturn(user);

        Optional<User> result = userService.findByRoleAndUsername(Role.DIRECTOR, "john");

        assertTrue(result.isPresent());
    }

    // FIND BY USERNAME
    @Test
    void testFindByUsername() {
        User user = new User();
        when(userRepository.findByUsername("admin")).thenReturn(user);

        User result = userService.findByUsername("admin");

        assertEquals(user, result);
    }
}

