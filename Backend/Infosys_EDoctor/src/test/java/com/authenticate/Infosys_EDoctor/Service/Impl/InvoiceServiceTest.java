package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;

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

@ContextConfiguration(classes = {InvoiceService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class InvoiceServiceTest {
    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private InvoiceService invoiceService;

    /**
     * Test {@link InvoiceService#generateInvoice(Long)}.
     * <p>
     * Method under test: {@link InvoiceService#generateInvoice(Long)}
     */
    @Test
    @DisplayName("Test generateInvoice(Long)")
    void testGenerateInvoice() throws UnsupportedEncodingException {
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

        // Act
        invoiceService.generateInvoice(1L);

        // Assert
        verify(appointmentService).getAppointmentById(eq(1L));
    }

    /**
     * Test {@link InvoiceService#generateInvoice(Long)}.
     * <ul>
     *   <li>Given {@link Appointment} (default constructor) AppointmentDateTime is
     * now atStartOfDay.</li>
     * </ul>
     * <p>
     * Method under test: {@link InvoiceService#generateInvoice(Long)}
     */
    @Test
    @DisplayName("Test generateInvoice(Long); given Appointment (default constructor) AppointmentDateTime is now atStartOfDay")
    void testGenerateInvoice_givenAppointmentAppointmentDateTimeIsNowAtStartOfDay() throws UnsupportedEncodingException {
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
        appointment.setAppointmentDateTime(LocalDate.now().atStartOfDay());
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

        // Act
        invoiceService.generateInvoice(1L);

        // Assert
        verify(appointmentService).getAppointmentById(eq(1L));
    }
}