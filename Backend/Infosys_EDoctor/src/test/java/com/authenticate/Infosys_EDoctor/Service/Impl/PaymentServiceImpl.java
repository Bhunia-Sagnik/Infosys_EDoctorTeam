package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Repository.AppointmentRepository;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PaymentService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PaymentServiceTest {
    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private InvoiceService invoiceService;

    @Autowired
    private PaymentService paymentService;

    /**
     * Test {@link PaymentService#initiatePayment(Long)}.
     * <ul>
     *   <li>Then throw {@link IllegalArgumentException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link PaymentService#initiatePayment(Long)}
     */
    @Test
    @DisplayName("Test initiatePayment(Long); then throw IllegalArgumentException")
    void testInitiatePayment_thenThrowIllegalArgumentException() throws Exception {
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
        Optional<Appointment> ofResult = Optional.of(appointment);
        when(appointmentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> paymentService.initiatePayment(1L));
        verify(appointmentRepository).findById(eq(1L));
    }

    /**
     * Test {@link PaymentService#verifyPayment(Long, String)}.
     * <p>
     * Method under test: {@link PaymentService#verifyPayment(Long, String)}
     */
    @Test
    @DisplayName("Test verifyPayment(Long, String)")
    @Disabled("TODO: Complete this test")
    void testVerifyPayment() throws Exception {
        try (MockedStatic<InetAddress> mockInetAddress = mockStatic(InetAddress.class)) {
            // TODO: Diffblue Cover was only able to create a partial test for this method:
            //   Reason: No inputs found that don't throw a trivial exception.
            //   Diffblue Cover tried to run the arrange/act section, but the method under
            //   test threw
            //   java.lang.NullPointerException: host can't be null
            //       at java.base/java.lang.SecurityManager.checkConnect(SecurityManager.java:896)
            //       at java.base/java.net.Socket.connect(Socket.java:754)
            //       at okhttp3.internal.platform.Platform.connectSocket(Platform.kt:128)
            //       at okhttp3.internal.connection.RealConnection.connectSocket(RealConnection.kt:295)
            //       at okhttp3.internal.connection.RealConnection.connect(RealConnection.kt:207)
            //       at okhttp3.internal.connection.ExchangeFinder.findConnection(ExchangeFinder.kt:226)
            //       at okhttp3.internal.connection.ExchangeFinder.findHealthyConnection(ExchangeFinder.kt:106)
            //       at okhttp3.internal.connection.ExchangeFinder.find(ExchangeFinder.kt:74)
            //       at okhttp3.internal.connection.RealCall.initExchange$okhttp(RealCall.kt:255)
            //       at okhttp3.internal.connection.ConnectInterceptor.intercept(ConnectInterceptor.kt:32)
            //       at okhttp3.internal.http.RealInterceptorChain.proceed(RealInterceptorChain.kt:109)
            //       at okhttp3.internal.cache.CacheInterceptor.intercept(CacheInterceptor.kt:95)
            //       at okhttp3.internal.http.RealInterceptorChain.proceed(RealInterceptorChain.kt:109)
            //       at okhttp3.internal.http.BridgeInterceptor.intercept(BridgeInterceptor.kt:83)
            //       at okhttp3.internal.http.RealInterceptorChain.proceed(RealInterceptorChain.kt:109)
            //       at okhttp3.internal.http.RetryAndFollowUpInterceptor.intercept(RetryAndFollowUpInterceptor.kt:76)
            //       at okhttp3.internal.http.RealInterceptorChain.proceed(RealInterceptorChain.kt:109)
            //       at okhttp3.logging.HttpLoggingInterceptor.intercept(HttpLoggingInterceptor.kt:154)
            //       at okhttp3.internal.http.RealInterceptorChain.proceed(RealInterceptorChain.kt:109)
            //       at okhttp3.internal.connection.RealCall.getResponseWithInterceptorChain$okhttp(RealCall.kt:201)
            //       at okhttp3.internal.connection.RealCall.execute(RealCall.kt:154)
            //       at com.razorpay.ApiUtils.processRequest(ApiUtils.java:133)
            //       at com.razorpay.ApiUtils.getRequest(ApiUtils.java:98)
            //       at com.razorpay.ApiClient.get(ApiClient.java:34)
            //       at com.razorpay.PaymentClient.fetch(PaymentClient.java:19)
            //       at com.authenticate.Infosys_EDoctor.Service.Impl.PaymentService.verifyPayment(PaymentService.java:80)
            //   See https://diff.blue/R013 to resolve this issue.

            // Arrange
            mockInetAddress.when(() -> InetAddress.getAllByName(Mockito.<String>any())).thenReturn(new InetAddress[]{null});

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
            paymentService.verifyPayment(1L, "42");
        }
    }

    /**
     * Test {@link PaymentService#getInvoice(Long)}.
     * <p>
     * Method under test: {@link PaymentService#getInvoice(Long)}
     */
    @Test
    @DisplayName("Test getInvoice(Long)")
    void testGetInvoice() throws IOException {
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
        ByteArrayInputStream actualInvoice = paymentService.getInvoice(1L);

        // Assert
        verify(appointmentService).getAppointmentById(eq(1L));
        byte[] byteArray = new byte[8];
        assertEquals(8, actualInvoice.read(byteArray));
        assertArrayEquals("AXAXAXAX".getBytes("UTF-8"), byteArray);
    }
}