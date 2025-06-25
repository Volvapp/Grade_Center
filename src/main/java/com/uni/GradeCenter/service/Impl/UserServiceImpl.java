package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public void updateUserFromDTO(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }

        userRepository.save(user);
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return this.userRepository.findAllByRole(role);
    }
}
