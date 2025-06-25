package com.uni.GradeCenter.service;

import com.uni.GradeCenter.model.User;

public interface UserService {
    User createUser(User user);

    User getUserById(Long id);

    User updateUser(User user);

    void deleteUserById(Long id);

    boolean usernameOrEmailExists(String username, String email);
}
