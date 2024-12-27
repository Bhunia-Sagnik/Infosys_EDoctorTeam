package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Controller.AuthController;
import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class AuthControllerServiceImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("testemail@example.com");
        user.setRole(User.Role.PATIENT); // assuming a patient role for the test
        user.setEnabled(false); // initially not enabled

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"testuser\",\"password\":\"testpassword\",\"email\":\"testemail@example.com\",\"role\":\"PATIENT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testemail@example.com"))
                .andExpect(jsonPath("$.role").value("PATIENT"))
                .andExpect(jsonPath("$.enabled").value(false));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    public void testLoginUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("testemail@example.com");
        user.setRole(User.Role.PATIENT);

        when(userService.loginUser("testuser", "testpassword")).thenReturn(user);

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"testuser\",\"password\":\"testpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testemail@example.com"))
                .andExpect(jsonPath("$.role").value("PATIENT"));

        verify(userService, times(1)).loginUser("testuser", "testpassword");
    }

    @Test
    public void testVerifyEmail_Success() throws Exception {
        when(userService.verifyEmail("123456", "testuser")).thenReturn(true);

        mockMvc.perform(post("/api/auth/verify-email")
                        .param("code", "123456")
                        .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email verified successfully"));

        verify(userService, times(1)).verifyEmail("123456", "testuser");
    }

    @Test
    public void testVerifyEmail_Failure() throws Exception {
        when(userService.verifyEmail("123456", "testuser")).thenReturn(false);

        mockMvc.perform(post("/api/auth/verify-email")
                        .param("code", "123456")
                        .param("username", "testuser"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Enter valid OTP"));

        verify(userService, times(1)).verifyEmail("123456", "testuser");
    }

    @Test
    public void testSendResetPasswordToken_Success() throws Exception {
        when(userService.sendResetPasswordToken("testemail@example.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/reset-password")
                        .param("email", "testemail@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reset password OTP has been successfully sent to your email"));

        verify(userService, times(1)).sendResetPasswordToken("testemail@example.com");
    }

    @Test
    public void testSendResetPasswordToken_Failure() throws Exception {
        when(userService.sendResetPasswordToken("testemail@example.com")).thenReturn(false);

        mockMvc.perform(post("/api/auth/reset-password")
                        .param("email", "testemail@example.com"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Enter valid OTP and password"));

        verify(userService, times(1)).sendResetPasswordToken("testemail@example.com");
    }

    @Test
    public void testResetPassword_Success() throws Exception {
        when(userService.resetPassword("testemail@example.com", "token123", "newpassword")).thenReturn(true);

        mockMvc.perform(post("/api/auth/reset-password/confirm")
                        .param("email", "testemail@example.com")
                        .param("token", "token123")
                        .param("newPassword", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("Your password has been successfully changes"));

        verify(userService, times(1)).resetPassword("testemail@example.com", "token123", "newpassword");
    }

    @Test
    public void testResetPassword_Failure() throws Exception {
        when(userService.resetPassword("testemail@example.com", "token123", "newpassword")).thenReturn(false);

        mockMvc.perform(post("/api/auth/reset-password/confirm")
                        .param("email", "testemail@example.com")
                        .param("token", "token123")
                        .param("newPassword", "newpassword"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Enter valid email address"));

        verify(userService, times(1)).resetPassword("testemail@example.com", "token123", "newpassword");
    }
}


