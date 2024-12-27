package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@Valid @RequestBody Map<String, String> loginDetails) {
        String username = loginDetails.get("username");
        String password = loginDetails.get("password");

        User user = userService.loginUser(username, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(401).body(null); // Unauthorized
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("code") String verificationCode, @RequestParam("username") String username) {
        boolean validated = userService.verifyEmail(verificationCode, username);
        return validated? ResponseEntity.ok("Email verified successfully")
                : ResponseEntity.badRequest().body("Enter valid OTP");
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email, @RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        boolean resetPasswordEmailSent = userService.resetPassword(email, token, newPassword);
        return resetPasswordEmailSent? ResponseEntity.ok("Your password has been successfully changes")
                :ResponseEntity.badRequest().body("Enter valid email address");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> sendResetPasswordToken(@RequestParam("email") String email) {
        boolean passwordReset = userService.sendResetPasswordToken(email);

        return passwordReset? ResponseEntity.ok("Reset password OTP has been successfully sent to your email")
                :ResponseEntity.badRequest().body("Enter valid OTP and password");
    }
}
