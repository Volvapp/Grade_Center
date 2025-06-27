package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.*;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ParentService parentService;
    private final AbsenceService absenceService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TeacherService teacherService, StudentService studentService, ParentService parentService, AbsenceService absenceService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.parentService = parentService;
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

        User admin = new User(
                "admin",
                passwordEncoder.encode("1234"),
                "Admin",
                "User",
                "admin@example.com",
                Role.ADMIN
        );

        User director = new User(
                "director",
                passwordEncoder.encode("1234"),
                "Diana",
                "Director",
                "director@example.com",
                Role.DIRECTOR
        );

        User director2 = new User(
                "director2",
                passwordEncoder.encode("1234"),
                "Zlatko",
                "Director",
                "director2@example.com",
                Role.DIRECTOR
        );

        User director3 = new User(
                "director3",
                passwordEncoder.encode("1234"),
                "Guliver",
                "Director",
                "director3@example.com",
                Role.DIRECTOR
        );

        User director4 = new User(
                "director4",
                passwordEncoder.encode("1234"),
                "Yonko",
                "Director",
                "director4@example.com",
                Role.DIRECTOR
        );

        User teacher = new User(
                "teacher",
                passwordEncoder.encode("1234"),
                "Tom",
                "Teacher",
                "teacher@example.com",
                Role.TEACHER
        );

        User teacher2 = new User(
                "teacher2",
                passwordEncoder.encode("1234"),
                "Zeus",
                "Teacher",
                "teacher2@example.com",
                Role.TEACHER
        );

        User teacher3 = new User(
                "teacher3",
                passwordEncoder.encode("1234"),
                "Loki",
                "Teacher",
                "teacher3@example.com",
                Role.TEACHER
        );

        User teacher4 = new User(
                "teacher4",
                passwordEncoder.encode("1234"),
                "Aristotel",
                "Teacher",
                "teacher4@example.com",
                Role.TEACHER
        );

        User student = new User(
                "student",
                passwordEncoder.encode("1234"),
                "Sara",
                "Student",
                "student@example.com",
                Role.STUDENT
        );

        User student2 = new User(
                "student2",
                passwordEncoder.encode("1234"),
                "Yosif",
                "Student",
                "student2@example.com",
                Role.STUDENT
        );

        User student3 = new User(
                "student3",
                passwordEncoder.encode("1234"),
                "Goril",
                "Student",
                "student3@example.com",
                Role.STUDENT
        );

        User student4 = new User(
                "student4",
                passwordEncoder.encode("1234"),
                "Ivaila",
                "Student",
                "student4@example.com",
                Role.STUDENT
        );

        User parent = new User(
                "parent",
                passwordEncoder.encode("1234"),
                "Peter",
                "Parent",
                "parent@example.com",
                Role.PARENT
        );

        User parent2 = new User(
                "parent2",
                passwordEncoder.encode("1234"),
                "Dragan",
                "Parent",
                "parent2@example.com",
                Role.PARENT
        );

        User parent3 = new User(
                "parent3",
                passwordEncoder.encode("1234"),
                "Igor",
                "Parent",
                "parent3@example.com",
                Role.PARENT
        );

        User parent4 = new User(
                "parent4",
                passwordEncoder.encode("1234"),
                "Oliver",
                "Parent",
                "parent4@example.com",
                Role.PARENT
        );

        userRepository.saveAll(List.of(admin, director, teacher, student, parent));
    }
    @Override
    @Transactional
    public void updateUserFromDTO(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role oldRole = user.getRole();

        updateBasicUserInfo(user, userDTO);

        if (userDTO.getRole() != null && userDTO.getRole() != oldRole) {
            removeOldRoleRelations(id, oldRole);
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
}
