package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {EmailService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class EmailServiceTest {
    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender javaMailSender;

    /**
     * Test {@link EmailService#sendEmail(String, String, String)}.
     * <p>
     * Method under test: {@link EmailService#sendEmail(String, String, String)}
     */
    @Test
    @DisplayName("Test sendEmail(String, String, String)")
    void testSendEmail() throws MailException {
        // Arrange
        doNothing().when(javaMailSender).send(Mockito.<SimpleMailMessage>any());

        // Act
        emailService.sendEmail("alice.liddell@example.org", "Hello from the Dreaming Spires",
                "Not all who wander are lost");

        // Assert
        verify(javaMailSender).send(isA(SimpleMailMessage.class));
    }

    /**
     * Test {@link EmailService#sendDoctorIdEmail(String, String)}.
     * <p>
     * Method under test: {@link EmailService#sendDoctorIdEmail(String, String)}
     */
    @Test
    @DisplayName("Test sendDoctorIdEmail(String, String)")
    void testSendDoctorIdEmail() throws MailException {
        // Arrange
        doNothing().when(javaMailSender).send(Mockito.<SimpleMailMessage>any());

        // Act
        emailService.sendDoctorIdEmail("jane.doe@example.org", "42");

        // Assert
        verify(javaMailSender).send(isA(SimpleMailMessage.class));
    }

    /**
     * Test {@link EmailService#sendPatientIdEmail(String, String)}.
     * <p>
     * Method under test: {@link EmailService#sendPatientIdEmail(String, String)}
     */
    @Test
    @DisplayName("Test sendPatientIdEmail(String, String)")
    void testSendPatientIdEmail() throws MailException {
        // Arrange
        doNothing().when(javaMailSender).send(Mockito.<SimpleMailMessage>any());

        // Act
        emailService.sendPatientIdEmail("jane.doe@example.org", "42");

        // Assert
        verify(javaMailSender).send(isA(SimpleMailMessage.class));
    }
}
