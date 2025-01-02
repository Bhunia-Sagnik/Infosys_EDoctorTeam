package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User registeredUser = userService.registerUser(user);

        // Assert
        assertEquals("testUser", registeredUser.getUsername());
        verify(emailService, times(1)).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        // Arrange
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.registerUser(user));
    }

    @Test
    void testLoginUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setEnabled(true);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        // Act
        User loggedInUser = userService.loginUser("testUser", "password123");

        // Assert
        assertEquals("testUser", loggedInUser.getUsername());
    }

    @Test
    void testLoginUser_InvalidPassword() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setEnabled(true);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.loginUser("testUser", "wrongPassword"));
    }

    @Test
    void testLoginUser_EmailNotVerified() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setEnabled(false);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.loginUser("testUser", "password123"));
    }

    @Test
    void testVerifyEmail_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setVerificationCode("1234");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.verifyEmail("1234", "testUser");

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testVerifyEmail_InvalidCode() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setVerificationCode("1234");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.verifyEmail("5678", "testUser"));
    }

    @Test
    void testSendResetPasswordToken_Success() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.sendResetPasswordToken("test@example.com");

        // Assert
        assertTrue(result);
        verify(emailService, times(1)).sendEmail(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    void testSendResetPasswordToken_InvalidEmail() {
        // Arrange
        when(userRepository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.sendResetPasswordToken("invalid@example.com"));
    }

    @Test
    void testResetPassword_Success() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setResetPasswordToken("1234");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // Act
        boolean result = userService.resetPassword("test@example.com", "1234", "newPassword");

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testResetPassword_InvalidToken() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setResetPasswordToken("1234");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.resetPassword("test@example.com", "5678", "newPassword"));
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Act
        var userDetails = userService.loadUserByUsername("testUser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("unknownUser"));
    }
}