package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.dto.UserDTO;
import com.uni.GradeCenter.model.enums.Role;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User getUserById(Long id);

    User updateUser(User user);

    void deleteUserById(Long id);

    List<User> getAllUsers();

    boolean usernameOrEmailExists(String username, String email);

    void initializeUsers();

    void updateUserFromDTO(Long id, UserDTO userDTO);

    User getUsersByRole(Role role);
}
