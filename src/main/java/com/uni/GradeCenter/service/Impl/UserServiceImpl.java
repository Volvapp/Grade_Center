package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ParentService parentService;
    private final SchoolService schoolService;
    private final AbsenceService absenceService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TeacherService teacherService, StudentService studentService, ParentService parentService, SchoolService schoolService, AbsenceService absenceService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.parentService = parentService;
        this.schoolService = schoolService;
        this.absenceService = absenceService;
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow();
    }

    @Override
    public User updateUser(User user) {
        User currentUser = this.userRepository.findById(user.getId()).orElseThrow();

        return this.userRepository.save(currentUser);
    }

    @Override
    public Optional<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public boolean usernameOrEmailExists(String username, String email) {
        return this.userRepository.findByUsername(username) != null || this.userRepository.findByEmail(email) != null;
    }

    @Override
    public void initializeUsers() {
        if (userRepository.count() > 0) return;

        List<User> usersToSave = List.of(
                newUser("admin", "Admin", "User", "admin@example.com", Role.ADMIN),
                newUser("director", "Diana", "Director", "director@example.com", Role.DIRECTOR),
                newUser("director2", "Zlatko", "Director", "director2@example.com", Role.DIRECTOR),
                newUser("director3", "Guliver", "Director", "director3@example.com", Role.DIRECTOR),
                newUser("director4", "Yonko", "Director", "director4@example.com", Role.DIRECTOR),
                newUser("teacher", "Tom", "Teacher", "teacher@example.com", Role.TEACHER),
                newUser("teacher2", "Zeus", "Teacher", "teacher2@example.com", Role.TEACHER),
                newUser("teacher3", "Loki", "Teacher", "teacher3@example.com", Role.TEACHER),
                newUser("teacher4", "Aristotel", "Teacher", "teacher4@example.com", Role.TEACHER),
                newUser("student", "Sara", "Student", "student@example.com", Role.STUDENT),
                newUser("student2", "Yosif", "Student", "student2@example.com", Role.STUDENT),
                newUser("student3", "Goril", "Student", "student3@example.com", Role.STUDENT),
                newUser("student4", "Ivaila", "Student", "student4@example.com", Role.STUDENT),
                newUser("parent", "Peter", "Parent", "parent@example.com", Role.PARENT),
                newUser("parent2", "Dragan", "Parent", "parent2@example.com", Role.PARENT),
                newUser("parent3", "Igor", "Parent", "parent3@example.com", Role.PARENT),
                newUser("parent4", "Oliver", "Parent", "parent4@example.com", Role.PARENT)
        );

        userRepository.saveAll(usersToSave);
    }

    private User newUser(String username, String firstName, String lastName, String email, Role role) {
        return new User(
                username,
                passwordEncoder.encode("1234"),
                firstName,
                lastName,
                email,
                role
        );
    }

    @Override
    @Transactional
    public void updateUserFromDTO(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role oldRole = user.getRole();

        updateBasicUserInfo(user, userDTO);

        if (userDTO.getRole() != null && userDTO.getRole() != oldRole) {
            if (oldRole != null) {
                removeOldRoleRelations(id, oldRole);
            }
            assignNewRole(user, userDTO.getRole());
            user.setRole(userDTO.getRole());
        }

        userRepository.save(user);
    }

    private void updateBasicUserInfo(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
    }

    private void removeOldRoleRelations(Long userId, Role oldRole) {
        switch (oldRole) {
            case STUDENT -> removeStudentRelations(userId);
            case TEACHER -> removeTeacherRelations(userId);
            case PARENT -> parentService.deleteByUserId(userId);
        }
    }

    private void removeStudentRelations(Long userId) {
        Student student = studentService.getStudentByUserId(userId);
        if (student.getParent() != null) {
            Parent parent = student.getParent();
            parent.setChild(null);
            parentService.updateParent(parent);
        }
        studentService.deleteByUserId(userId);
    }

    private void removeTeacherRelations(Long userId) {
        Teacher teacher = teacherService.getTeacherByUserId(userId);

        if (teacher.getSchool() != null) {
            School school = teacher.getSchool();
            teacher.setSchool(null);
            school.getTeachers().remove(teacher);
        }

        teacher.getSchedules().forEach(schedule -> schedule.setTeacher(null));
        teacher.getSchedules().clear();

        teacher.getGrades().forEach(grade -> grade.setTeacher(null));
        teacher.getGrades().clear();

        absenceService.getAllAbsences().stream()
                .filter(absence -> absence.getTeacher() == teacher)
                .forEach(absence -> absence.setTeacher(null));

        teacherService.updateTeacher(teacher);
        teacherService.deleteByUserId(userId);
    }

    private void assignNewRole(User user, Role newRole) {
        switch (newRole) {
            case STUDENT -> {
                Student student = new Student();
                student.setUser(user);
                studentService.createStudent(student);
            }
            case TEACHER -> {
                Teacher teacher = new Teacher();
                teacher.setUser(user);
                teacherService.createTeacher(teacher);
            }
            case PARENT -> {
                Parent parent = new Parent();
                parent.setUser(user);
                parentService.createParent(parent);
            }
        }
    }


    @Override
    public List<User> getUsersByRole(Role role) {
        return this.userRepository.findAllByRole(role);
    }

    @Override
    public Optional<User> findByRoleAndUsername(Role role, String director) {
        return userRepository.findByRoleAndUsername(role, director);
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }


}
