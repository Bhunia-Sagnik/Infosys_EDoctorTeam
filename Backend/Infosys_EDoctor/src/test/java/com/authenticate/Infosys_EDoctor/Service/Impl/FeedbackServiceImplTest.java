package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.Feedback;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Repository.FeedbackRepository;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {FeedbackServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class FeedbackServiceImplTest {
    @MockBean
    private DoctorService doctorService;

    @MockBean
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackServiceImpl feedbackServiceImpl;

    /**
     * Test {@link FeedbackServiceImpl#submitFeedback(Feedback)}.
     * <p>
     * Method under test: {@link FeedbackServiceImpl#submitFeedback(Feedback)}
     */
    @Test
    @DisplayName("Test submitFeedback(Feedback)")
    void testSubmitFeedback() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setChargedPerVisit(1);
        doctor.setDoctorId("42");
        doctor.setEmail("jane.doe@example.org");
        doctor.setHospitalName("Hospital Name");
        doctor.setLocation("Location");
        doctor.setMobileNo("Mobile No");
        doctor.setName("Name");
        doctor.setPassword("iloveyou");
        doctor.setSpecialization("Specialization");

        Patient patient = new Patient();
        patient.setAddress("42 Main St");
        patient.setAge(1);
        patient.setBloodGroup("Blood Group");
        patient.setEmail("jane.doe@example.org");
        patient.setGender(Patient.Gender.MALE);
        patient.setMobileNo("Mobile No");
        patient.setName("Name");
        patient.setPassword("iloveyou");
        patient.setPatientId("42");

        Feedback feedback = new Feedback();
        feedback.setDoctor(doctor);
        feedback.setFeedbackText("Feedback Text");
        feedback.setId(1L);
        feedback.setPatient(patient);
        feedback.setRating(1);
        when(feedbackRepository.save(Mockito.<Feedback>any())).thenReturn(feedback);

        Doctor doctor2 = new Doctor();
        doctor2.setChargedPerVisit(1);
        doctor2.setDoctorId("42");
        doctor2.setEmail("jane.doe@example.org");
        doctor2.setHospitalName("Hospital Name");
        doctor2.setLocation("Location");
        doctor2.setMobileNo("Mobile No");
        doctor2.setName("Name");
        doctor2.setPassword("iloveyou");
        doctor2.setSpecialization("Specialization");

        Patient patient2 = new Patient();
        patient2.setAddress("42 Main St");
        patient2.setAge(1);
        patient2.setBloodGroup("Blood Group");
        patient2.setEmail("jane.doe@example.org");
        patient2.setGender(Patient.Gender.MALE);
        patient2.setMobileNo("Mobile No");
        patient2.setName("Name");
        patient2.setPassword("iloveyou");
        patient2.setPatientId("42");

        Feedback feedback2 = new Feedback();
        feedback2.setDoctor(doctor2);
        feedback2.setFeedbackText("Feedback Text");
        feedback2.setId(1L);
        feedback2.setPatient(patient2);
        feedback2.setRating(1);

        // Act
        Feedback actualSubmitFeedbackResult = feedbackServiceImpl.submitFeedback(feedback2);

        // Assert
        verify(feedbackRepository).save(isA(Feedback.class));
        assertSame(feedback, actualSubmitFeedbackResult);
    }

    /**
     * Test {@link FeedbackServiceImpl#getFeedbackByDoctor(String)}.
     * <p>
     * Method under test: {@link FeedbackServiceImpl#getFeedbackByDoctor(String)}
     */
    @Test
    @DisplayName("Test getFeedbackByDoctor(String)")
    void testGetFeedbackByDoctor() {
        // Arrange
        when(feedbackRepository.findByDoctorId(Mockito.<String>any())).thenReturn(null);

        // Act
        List<Feedback> actualFeedbackByDoctor = feedbackServiceImpl.getFeedbackByDoctor("42");

        // Assert
        verify(feedbackRepository).findByDoctorId(eq("42"));
        assertNull(actualFeedbackByDoctor);
    }

    /**
     * Test {@link FeedbackServiceImpl#getFeedbackByPatient(String)}.
     * <p>
     * Method under test: {@link FeedbackServiceImpl#getFeedbackByPatient(String)}
     */
    @Test
    @DisplayName("Test getFeedbackByPatient(String)")
    void testGetFeedbackByPatient() {
        // Arrange
        when(feedbackRepository.findByPatientId(Mockito.<String>any())).thenReturn(null);

        // Act
        List<Feedback> actualFeedbackByPatient = feedbackServiceImpl.getFeedbackByPatient("42");

        // Assert
        verify(feedbackRepository).findByPatientId(eq("42"));
        assertNull(actualFeedbackByPatient);
    }

    /**
     * Test {@link FeedbackServiceImpl#getDoctorsWithFeedbackByPatient(String)}.
     * <p>
     * Method under test:
     * {@link FeedbackServiceImpl#getDoctorsWithFeedbackByPatient(String)}
     */
    @Test
    @DisplayName("Test getDoctorsWithFeedbackByPatient(String)")
    void testGetDoctorsWithFeedbackByPatient() {
        // Arrange
        when(feedbackRepository.findDoctorIdsByPatientId(Mockito.<String>any())).thenReturn(null);

        // Act
        List<String> actualDoctorsWithFeedbackByPatient = feedbackServiceImpl.getDoctorsWithFeedbackByPatient("42");

        // Assert
        verify(feedbackRepository).findDoctorIdsByPatientId(eq("42"));
        assertNull(actualDoctorsWithFeedbackByPatient);
    }

    /**
     * Test {@link FeedbackServiceImpl#getAvgFeedbackByDoctor(String)}.
     * <p>
     * Method under test: {@link FeedbackServiceImpl#getAvgFeedbackByDoctor(String)}
     */
    @Test
    @DisplayName("Test getAvgFeedbackByDoctor(String)")
    void testGetAvgFeedbackByDoctor() {
        // Arrange
        when(feedbackRepository.findAverageRatingByDoctorId(Mockito.<String>any())).thenReturn(10.0d);

        // Act
        Double actualAvgFeedbackByDoctor = feedbackServiceImpl.getAvgFeedbackByDoctor("42");

        // Assert
        verify(feedbackRepository).findAverageRatingByDoctorId(eq("42"));
        assertEquals(10.0d, actualAvgFeedbackByDoctor.doubleValue());
    }
}