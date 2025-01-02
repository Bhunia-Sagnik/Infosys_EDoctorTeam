package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;
import com.authenticate.Infosys_EDoctor.Service.PatientService;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {NotificationServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class NotificationServiceImplTest {
    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private EmailService emailService;

    @Autowired
    private NotificationServiceImpl notificationServiceImpl;

    @MockBean
    private PatientService patientService;

    /**
     * Test
     * {@link NotificationServiceImpl#sendDoctorProfileCreatedNotification(String, String)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendDoctorProfileCreatedNotification(String, String)}
     */
    @Test
    @DisplayName("Test sendDoctorProfileCreatedNotification(String, String)")
    void testSendDoctorProfileCreatedNotification() {
        // Arrange
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        // Act
        notificationServiceImpl.sendDoctorProfileCreatedNotification("jane.doe@example.org", "42");

        // Assert
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Doctor Profile Created"), eq(
                "Welcome to EDoctor Application: Your one stop solution for all outpatient appointments\n\nYour Doctor ID is: 42\nSave this ID for further references."));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendPatientProfileCreatedNotification(String, String)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendPatientProfileCreatedNotification(String, String)}
     */
    @Test
    @DisplayName("Test sendPatientProfileCreatedNotification(String, String)")
    void testSendPatientProfileCreatedNotification() {
        // Arrange
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        // Act
        notificationServiceImpl.sendPatientProfileCreatedNotification("jane.doe@example.org", "42");

        // Assert
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Patient Profile Created"), eq(
                "Welcome to EDoctor Application: Your one stop solution for all outpatient appointments\n\nYour Patient ID is: 42\nSave this ID for further references."));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendAdminProfileCreatedNotification(String, String)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendAdminProfileCreatedNotification(String, String)}
     */
    @Test
    @DisplayName("Test sendAdminProfileCreatedNotification(String, String)")
    void testSendAdminProfileCreatedNotification() {
        // Arrange
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        // Act
        notificationServiceImpl.sendAdminProfileCreatedNotification("jane.doe@example.org", "42");

        // Assert
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Admin Profile Created"), eq(
                "Welcome to EDoctor Application: Your one stop solution for all outpatient appointments\n\nYour Admin ID is: 42\nSave this ID for further references."));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendNewAppointmentNotificationToDoctor(Appointment)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendNewAppointmentNotificationToDoctor(Appointment)}
     */
    @Test
    @DisplayName("Test sendNewAppointmentNotificationToDoctor(Appointment)")
    void testSendNewAppointmentNotificationToDoctor() throws UnsupportedEncodingException {
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
        when(doctorService.getDoctorById(Mockito.<String>any())).thenReturn(doctor);
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

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
        when(patientService.getPatientById(Mockito.<String>any())).thenReturn(patient);

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

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor2);
        appointment.setFeedbackGiven(true);
        appointment.setInvoicePdf("AXAXAXAX".getBytes("UTF-8"));
        appointment.setPaid(true);
        appointment.setPatient(patient2);
        appointment.setPaymentId("42");
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);

        // Act
        notificationServiceImpl.sendNewAppointmentNotificationToDoctor(appointment);

        // Assert
        verify(doctorService).getDoctorById(eq("42"));
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("New Appointment Scheduled"),
                eq("Dear Dr. Name,\n\nA new appointment has been scheduled by Name on 1970-01-01 at 00:00.\n\nThank you."));
        verify(patientService).getPatientById(eq("42"));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendNewAppointmentNotificationToPatient(Appointment)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendNewAppointmentNotificationToPatient(Appointment)}
     */
    @Test
    @DisplayName("Test sendNewAppointmentNotificationToPatient(Appointment)")
    void testSendNewAppointmentNotificationToPatient() throws UnsupportedEncodingException {
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
        when(doctorService.getDoctorById(Mockito.<String>any())).thenReturn(doctor);
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

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
        when(patientService.getPatientById(Mockito.<String>any())).thenReturn(patient);

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

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor2);
        appointment.setFeedbackGiven(true);
        appointment.setInvoicePdf("AXAXAXAX".getBytes("UTF-8"));
        appointment.setPaid(true);
        appointment.setPatient(patient2);
        appointment.setPaymentId("42");
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);

        // Act
        notificationServiceImpl.sendNewAppointmentNotificationToPatient(appointment);

        // Assert
        verify(doctorService).getDoctorById(eq("42"));
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("New Appointment Scheduled"),
                eq("Dear Name,\n\nA new appointment has been scheduled with Dr. Name on 1970-01-01 at 00:00.\n\nThank you."));
        verify(patientService).getPatientById(eq("42"));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendUpdatedAppointmentNotificationToPatient(Appointment)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendUpdatedAppointmentNotificationToPatient(Appointment)}
     */
    @Test
    @DisplayName("Test sendUpdatedAppointmentNotificationToPatient(Appointment)")
    void testSendUpdatedAppointmentNotificationToPatient() throws UnsupportedEncodingException {
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
        when(doctorService.getDoctorById(Mockito.<String>any())).thenReturn(doctor);
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

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
        when(patientService.getPatientById(Mockito.<String>any())).thenReturn(patient);

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

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor2);
        appointment.setFeedbackGiven(true);
        appointment.setInvoicePdf("AXAXAXAX".getBytes("UTF-8"));
        appointment.setPaid(true);
        appointment.setPatient(patient2);
        appointment.setPaymentId("42");
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);

        // Act
        notificationServiceImpl.sendUpdatedAppointmentNotificationToPatient(appointment);

        // Assert
        verify(doctorService).getDoctorById(eq("42"));
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Appointment Updated"), eq(
                "Dear Name,\n\nYour appointment with Dr. Namehas been updated. \nNow the appointment is scheduled on 1970-01-01 at 00:00.\n\nThank you."));
        verify(patientService).getPatientById(eq("42"));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendAppointmentConfirmationToPatient(Appointment)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendAppointmentConfirmationToPatient(Appointment)}
     */
    @Test
    @DisplayName("Test sendAppointmentConfirmationToPatient(Appointment)")
    void testSendAppointmentConfirmationToPatient() throws UnsupportedEncodingException {
        // Arrange
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

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

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor);
        appointment.setFeedbackGiven(true);
        appointment.setInvoicePdf("AXAXAXAX".getBytes("UTF-8"));
        appointment.setPaid(true);
        appointment.setPatient(patient);
        appointment.setPaymentId("42");
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);

        // Act
        notificationServiceImpl.sendAppointmentConfirmationToPatient(appointment);

        // Assert
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Appointment Confirmed"), eq(
                "Dear Name,\n\nYour appointment with Dr. Name on 1970-01-01 at 00:00 has been confirmed.\nPlease proceed to payment to pay the fees associated.\n\nThank you."));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendAppointmentCancellationNotification(Long, String, boolean)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendAppointmentCancellationNotification(Long, String, boolean)}
     */
    @Test
    @DisplayName("Test sendAppointmentCancellationNotification(Long, String, boolean)")
    void testSendAppointmentCancellationNotification() throws UnsupportedEncodingException {
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

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor);
        appointment.setFeedbackGiven(true);
        appointment.setInvoicePdf("AXAXAXAX".getBytes("UTF-8"));
        appointment.setPaid(true);
        appointment.setPatient(patient);
        appointment.setPaymentId("42");
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);
        when(appointmentService.getAppointmentById(Mockito.<Long>any())).thenReturn(appointment);
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        // Act
        notificationServiceImpl.sendAppointmentCancellationNotification(1L, "Just cause", true);

        // Assert
        verify(appointmentService).getAppointmentById(eq(1L));
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Appointment Canceled"), eq(
                "Dear Name,\n\nThe appointment scheduled on 1970-01-01 at 00:00 with Dr. Name has been canceled.\n\nReason: Just cause\n\nSorry for the inconvenience caused."));
    }

    /**
     * Test {@link NotificationServiceImpl#sendAppointmentReminder(Appointment)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendAppointmentReminder(Appointment)}
     */
    @Test
    @DisplayName("Test sendAppointmentReminder(Appointment)")
    void testSendAppointmentReminder() throws UnsupportedEncodingException {
        // Arrange
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

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

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor);
        appointment.setFeedbackGiven(true);
        appointment.setInvoicePdf("AXAXAXAX".getBytes("UTF-8"));
        appointment.setPaid(true);
        appointment.setPatient(patient);
        appointment.setPaymentId("42");
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);

        // Act
        notificationServiceImpl.sendAppointmentReminder(appointment);

        // Assert
        verify(emailService, atLeast(1)).sendEmail(eq("jane.doe@example.org"), eq("Appointment Reminder"),
                Mockito.<String>any());
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendProfileUpdatedByAdminNotification(String, String)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendProfileUpdatedByAdminNotification(String, String)}
     */
    @Test
    @DisplayName("Test sendProfileUpdatedByAdminNotification(String, String)")
    void testSendProfileUpdatedByAdminNotification() {
        // Arrange
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        // Act
        notificationServiceImpl.sendProfileUpdatedByAdminNotification("jane.doe@example.org", "42");

        // Assert
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Profile Updated"), eq(
                "Welcome to EDoctor Application: Your one stop solution for all outpatient appointments\n\nYour profile with ID: 42\nis updated by admin."));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendAdminProfileUpdatedNotification(String, String)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendAdminProfileUpdatedNotification(String, String)}
     */
    @Test
    @DisplayName("Test sendAdminProfileUpdatedNotification(String, String)")
    void testSendAdminProfileUpdatedNotification() {
        // Arrange
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        // Act
        notificationServiceImpl.sendAdminProfileUpdatedNotification("jane.doe@example.org", "42");

        // Assert
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Admin Profile Updated"), eq(
                "Welcome to EDoctor Application: Your one stop solution for all outpatient appointments\n\nYour Admin profile with ID: 42\nis updated by successfully."));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendAdminProfileDeletedNotification(String, String)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendAdminProfileDeletedNotification(String, String)}
     */
    @Test
    @DisplayName("Test sendAdminProfileDeletedNotification(String, String)")
    void testSendAdminProfileDeletedNotification() {
        // Arrange
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        // Act
        notificationServiceImpl.sendAdminProfileDeletedNotification("jane.doe@example.org", "42");

        // Assert
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Admin Profile Deleted"), eq(
                "Welcome to EDoctor Application: Your one stop solution for all outpatient appointments\n\nYour Admin profile with ID: 42\n has been deleted."));
    }

    /**
     * Test
     * {@link NotificationServiceImpl#sendProfileDeletedByAdminNotification(String, String)}.
     * <p>
     * Method under test:
     * {@link NotificationServiceImpl#sendProfileDeletedByAdminNotification(String, String)}
     */
    @Test
    @DisplayName("Test sendProfileDeletedByAdminNotification(String, String)")
    void testSendProfileDeletedByAdminNotification() {
        // Arrange
        doNothing().when(emailService).sendEmail(Mockito.<String>any(), Mockito.<String>any(), Mockito.<String>any());

        // Act
        notificationServiceImpl.sendProfileDeletedByAdminNotification("jane.doe@example.org", "42");

        // Assert
        verify(emailService).sendEmail(eq("jane.doe@example.org"), eq("Profile Deleted"), eq(
                "Welcome to EDoctor Application: Your one stop solution for all outpatient appointments\n\nYour profile with ID: 42\nhas been deleted by admin."));
    }
}