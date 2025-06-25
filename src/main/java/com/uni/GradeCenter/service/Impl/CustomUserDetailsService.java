package com.uni.GradeCenter.service.Impl;

import com.uni.GradeCenter.exception.AccountPendingException;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Logger logger = LoggerFactory.getLogger(getClass());

        User user = userRepository.findByUsername(username);

        if (user == null) {
            logger.warn("Authentication failed: User not found -> {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (user.getRole() == null) {
            logger.warn("Authentication failed: Account pending approval -> {}", username);
            throw new AccountPendingException("Account is pending approval");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }

}
