package com.uni.GradeCenter.repository;

import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    Optional<User> findByRole(Role role);

    List<User> findAllByRole(Role role);

    Optional<User> findByRoleAndUsername(Role role, String director);
}
