package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Repository.UserRepository;
import com.authenticate.Infosys_EDoctor.Service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Override
    @Transactional
    public User registerUser(User user) {
        Optional<User> exist = userRepository.findByUsername(user.getUsername());
        if(exist.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt the password
        String verificationCode = generateOTP();
        user.setVerificationCode(verificationCode);
        User savedUser = userRepository.save(user);


        emailService.sendEmail(user.getEmail(), "Email Verification", "Your OTP is: " + verificationCode + "\n Enter the OTP in the application to verify email.");

        return savedUser;
    }

    @Override
    public User loginUser(@Valid String username, String password) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if(!existingUser.isEnabled()) {
            throw new RuntimeException("Verify your email before logging in");
        }

        if(passwordEncoder.matches(password, existingUser.getPassword())) {
            return existingUser;
        }
        else {
            throw new RuntimeException("Enter valid password");
        }
    }

    @Override
    public boolean verifyEmail(String verificationCode, String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        User user;

        if(userOpt.isPresent()) {
            user = userOpt.get();
        }
        else {
            throw new UsernameNotFoundException("Username not found");
        }

        if (!verificationCode.equals(user.getVerificationCode())) {
            throw new RuntimeException("Enter valid OTP");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean sendResetPasswordToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String resetPasswordToken = generateOTP();
            user.setResetPasswordToken(resetPasswordToken);
            userRepository.save(user);

            emailService.sendEmail(user.getEmail(), "Password Reset Request", "Give the otp and new password: " + resetPasswordToken);

            return true;
        } else {
            throw new RuntimeException("Invalid email");
        }
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.getReferenceById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with given email not found."));
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public boolean resetPassword(String email, String token, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user = userOpt.get();

        if(user.getResetPasswordToken().equals(token)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetPasswordToken(null);
            userRepository.save(user);
            return true;
        }
        else {
            throw new RuntimeException("Enter valid OTP");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), new ArrayList<>());
    }

    private static final String NUMBERS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public String generateOTP() {
        StringBuilder otp = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            otp.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }

        return otp.toString();
    }
}
