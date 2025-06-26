package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.Parent;
import com.uni.GradeCenter.model.Student;
import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.ParentService;
import com.uni.GradeCenter.service.StudentService;
import com.uni.GradeCenter.service.TeacherService;
import com.uni.GradeCenter.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final ParentService parentService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, TeacherService teacherService, StudentService studentService, ParentService parentService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.parentService = parentService;
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

        User teacher = new User(
                "teacher",
                passwordEncoder.encode("1234"),
                "Tom",
                "Teacher",
                "teacher@example.com",
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

        User parent = new User(
                "parent",
                passwordEncoder.encode("1234"),
                "Peter",
                "Parent",
                "parent@example.com",
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

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getRole() != null && userDTO.getRole() != oldRole) {

            switch (oldRole) {
                case STUDENT:
                    Student studentByUserId = studentService.getStudentByUserId(id);
                    Parent parent = studentByUserId.getParent();
                    if (parent != null) {
                        parent.setChild(null);
                        this.parentService.updateParent(parent);
                    }
                    studentService.deleteByUserId(user.getId());
                    break;
                case TEACHER:
                    teacherService.deleteByUserId(user.getId());
                    break;
                case PARENT:
                    //todo shte trqbva da se napravi za vsqka rolq :))))))))))
                    parentService.deleteByUserId(user.getId());
                    break;
            }

            switch (userDTO.getRole()) {
                case STUDENT:
                    Student student = new Student();
                    student.setUser(user);
                    studentService.createStudent(student);
                    break;
                case TEACHER:
                    Teacher teacher = new Teacher();
                    teacher.setUser(user);
                    teacherService.createTeacher(teacher);
                    break;
                case PARENT:
                    Parent parent = new Parent();
                    parent.setUser(user);
                    parentService.createParent(parent);
                    break;
            }

            user.setRole(userDTO.getRole());
        }

        userRepository.save(user);
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return this.userRepository.findAllByRole(role);
    }
}
