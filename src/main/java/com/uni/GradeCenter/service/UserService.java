package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.Teacher;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.enums.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    User getUserById(Long id);

    User updateUser(User user);

    Optional<User> findByRole(Role role);

    void deleteUserById(Long id);

    List<User> getAllUsers();

    boolean usernameOrEmailExists(String username, String email);

    void initializeUsers();

    void updateUserFromDTO(Long id, UserDTO userDTO);

    List<User> getUsersByRole(Role role);

    Optional<User> findByRoleAndUsername(Role role, String director);

    User findByUsername(String username);

    List<User> getUnassignedDirectors();

}
