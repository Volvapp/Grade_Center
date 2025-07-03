package com.uni.GradeCenter.service.impl;

import com.uni.GradeCenter.exception.AccountPendingException;
import com.uni.GradeCenter.model.User;
import com.uni.GradeCenter.model.enums.Role;
import com.uni.GradeCenter.repository.UserRepository;
import com.uni.GradeCenter.service.Impl.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testLoadUserByUsername_userExistsWithRole() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encoded-password");
        user.setRole(Role.TEACHER);

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encoded-password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER")));
    }

    @Test
    void testLoadUserByUsername_userNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("unknown"));
    }

    @Test
    void testLoadUserByUsername_userHasNoRole() {
        User user = new User();
        user.setUsername("pendinguser");
        user.setPassword("password");
        user.setRole(null);

        when(userRepository.findByUsername("pendinguser")).thenReturn(user);

        assertThrows(AccountPendingException.class,
                () -> customUserDetailsService.loadUserByUsername("pendinguser"));
    }
}

