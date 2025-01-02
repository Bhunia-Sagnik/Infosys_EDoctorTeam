package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.DTO.AppointmentRequest;
import com.authenticate.Infosys_EDoctor.DTO.WebStatsBetweenDTO;
import com.authenticate.Infosys_EDoctor.DTO.WebStatsDTO;
import com.authenticate.Infosys_EDoctor.Entity.*;
import com.authenticate.Infosys_EDoctor.Repository.AdminRepository;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;
import com.authenticate.Infosys_EDoctor.Service.PatientService;
import com.authenticate.Infosys_EDoctor.Service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AdminServiceImpl.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AdminServiceImplTest {
    @MockBean
    private AdminRepository adminRepository;

    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private PatientService patientService;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("Test addAdmin(Admin); given AdminRepository save(Object) return Admin (default constructor); then return Admin (default constructor)")
    void testAddAdmin_givenAdminRepositorySaveReturnAdmin_thenReturnAdmin() {
        Admin admin = new Admin();
        admin.setAdminId("42");
        admin.setEmail("jane.doe@example.org");
        admin.setMobileNo("Mobile No");
        admin.setName("Name");
        admin.setPassword("iloveyou");
        when(adminRepository.save(Mockito.<Admin>any())).thenReturn(admin);
        Optional<Admin> emptyResult = Optional.empty();
        when(adminRepository.findByEmail(Mockito.<String>any())).thenReturn(emptyResult);

        Admin admin2 = new Admin();
        admin2.setAdminId("42");
        admin2.setEmail("jane.doe@example.org");
        admin2.setMobileNo("Mobile No");
        admin2.setName("Name");
        admin2.setPassword("iloveyou");

        Admin actualAddAdminResult = adminServiceImpl.addAdmin(admin2);

        verify(adminRepository).findByEmail(eq("jane.doe@example.org"));
        verify(adminRepository).save(isA(Admin.class));
        assertSame(admin, actualAddAdminResult);
    }

    @Test
    @DisplayName("Test addAdmin(Admin); then throw RuntimeException")
    void testAddAdmin_thenThrowRuntimeException() {
        Admin admin = new Admin();
        admin.setAdminId("42");
        admin.setEmail("jane.doe@example.org");
        admin.setMobileNo("Mobile No");
        admin.setName("Name");
        admin.setPassword("iloveyou");
        Optional<Admin> ofResult = Optional.of(admin);
        when(adminRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

        Admin admin2 = new Admin();
        admin2.setAdminId("42");
        admin2.setEmail("jane.doe@example.org");
        admin2.setMobileNo("Mobile No");
        admin2.setName("Name");
        admin2.setPassword("iloveyou");

        assertThrows(RuntimeException.class, () -> adminServiceImpl.addAdmin(admin2));
        verify(adminRepository).findByEmail(eq("jane.doe@example.org"));
    }

    @Test
    @DisplayName("Test updateAdmin(String, Admin)")
    void testUpdateAdmin() {
        Admin updatedAdmin = new Admin();
        updatedAdmin.setAdminId("42");
        updatedAdmin.setEmail("jane.doe@example.org");
        updatedAdmin.setMobileNo("Mobile No");
        updatedAdmin.setName("Name");
        updatedAdmin.setPassword("iloveyou");
        when(adminRepository.findById(Mockito.<String>any())).thenReturn(Optional.of(updatedAdmin));
        when(adminRepository.save(Mockito.<Admin>any())).thenReturn(updatedAdmin);

        adminServiceImpl.updateAdmin("42", updatedAdmin);

        verify(adminRepository).findById(eq("42"));
        verify(adminRepository).save(isA(Admin.class));
    }

    @Test
    @DisplayName("Test verifyAdmin(String)")
    void testVerifyAdmin() {
        Admin admin = new Admin();
        admin.setAdminId("42");
        admin.setEmail("jane.doe@example.org");
        when(adminRepository.findById(Mockito.<String>any())).thenReturn(Optional.of(admin));

        adminServiceImpl.verifyAdmin("42");

        verify(adminRepository).findById(eq("42"));
    }

    @Test
    @DisplayName("Test deleteAdmin(String)")
    void testDeleteAdmin() {
        Admin admin = new Admin();
        admin.setAdminId("42");
        admin.setEmail("jane.doe@example.org");
        when(adminRepository.findById(Mockito.<String>any())).thenReturn(Optional.of(admin));

        adminServiceImpl.deleteAdmin("42");

        verify(adminRepository).findById(eq("42"));
        verify(adminRepository).delete(isA(Admin.class));
    }

    @Test
    @DisplayName("Test getAllPatients()")
    void testGetAllPatients() {
        adminServiceImpl.getAllPatients();
        verify(patientService).getAllPatients();
    }

    @Test
    @DisplayName("Test updatePatient(String, Patient)")
    void testUpdatePatient() {
        Patient patient = new Patient();
        patient.setPatientId("42");
        patient.setName("John Doe");
        when(patientService.updateProfile(Mockito.<String>any(), Mockito.<Patient>any())).thenReturn(patient);

        adminServiceImpl.updatePatient("42", patient);

        verify(patientService).updateProfile(eq("42"), isA(Patient.class));
    }

    @Test
    @DisplayName("Test deletePatient(String)")
    void testDeletePatient() {
        adminServiceImpl.deletePatient("42");
        verify(patientService).deletePatient(eq("42"));
    }

    @Test
    @DisplayName("Test getAllDoctors()")
    void testGetAllDoctors() {
        adminServiceImpl.getAllDoctors();
        verify(doctorService).findAllDoctors();
    }

    @Test
    @DisplayName("Test updateDoctor(String, Doctor)")
    void testUpdateDoctor() {
        Doctor doctor = new Doctor();
        doctor.setDoctorId("42");
        doctor.setName("Dr. John");
        when(doctorService.updateDoctor(Mockito.<String>any(), Mockito.<Doctor>any())).thenReturn(doctor);

        adminServiceImpl.updateDoctor("42", doctor);

        verify(doctorService).updateDoctor(eq("42"), isA(Doctor.class));
    }

    @Test
    @DisplayName("Test deleteDoctor(String)")
    void testDeleteDoctor() {
        adminServiceImpl.deleteDoctor("42");
        verify(doctorService).deleteDoctor(eq("42"));
    }

    @Test
    @DisplayName("Test getAllAppointments()")
    void testGetAllAppointments() {
        adminServiceImpl.getAllAppointments();
        verify(appointmentService).getAllAppointments();
    }

    @Test
    @DisplayName("Test getAppointmentByPatientId(String)")
    void testGetAppointmentByPatientId() {
        adminServiceImpl.getAppointmentByPatientId("42");
        verify(appointmentService).getAppointmentsForPatient(eq("42"));
    }

    @Test
    @DisplayName("Test getAppointmentByDoctorId(String)")
    void testGetAppointmentByDoctorId() {
        adminServiceImpl.getAppointmentByDoctorId("42");
        verify(appointmentService).getAppointmentsForDoctor(eq("42"));
    }

    @Test
    @DisplayName("Test updateAppointment(Long, AppointmentRequest)")
    void testUpdateAppointment() {
        // Arrange
        // Mock existing appointment data to simulate getting an existing appointment
        Appointment existingAppointment = new Appointment();
        existingAppointment.setAppointmentId(1L);
        existingAppointment.setDoctor(new Doctor()); // Assuming a doctor entity exists
        existingAppointment.setPatient(new Patient()); // Assuming a patient entity exists
        existingAppointment.setStatus(Appointment.Status.Confirmed);
        existingAppointment.setReason("Initial reason");
        existingAppointment.setAppointmentDateTime(LocalDateTime.of(2025, 1, 2, 7, 30, 1, 422000000));

        // Mock the appointmentService to return the existing appointment when queried by ID
        when(appointmentService.getAppointmentById(1L)).thenReturn(existingAppointment);

        // Create the AppointmentRequest with updated data
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAppointmentDateTime("2025-01-02T07:30:01.422269");
        appointmentRequest.setDoctorId("42");
        appointmentRequest.setPatientId("42");
        appointmentRequest.setReason("Cause");

        // Act
        adminServiceImpl.updateAppointment(existingAppointment.getAppointmentId(), appointmentRequest);

        // Assert
        // Verify that the appointmentService.updateAppointment method is called with the correct parameters
        verify(appointmentService).updateAppointment(eq(existingAppointment.getAppointmentId()), argThat(updatedAppointment -> {
            // Check if the reason has been updated correctly
            return updatedAppointment.getReason().equals("Cause") &&
                    updatedAppointment.getAppointmentDateTime().equals(LocalDateTime.parse(appointmentRequest.getAppointmentDateTime()));
        }));
    }

    @Test
    @DisplayName("Test addAppointment(AppointmentRequest)")
    void testAddAppointment() {
        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setAppointmentDateTime("2025-01-02T07:30:01.422269");
        appointmentRequest.setDoctorId("42");
        appointmentRequest.setPatientId("42");
        appointmentRequest.setReason("Just cause");
        adminServiceImpl.addAppointment(appointmentRequest);
        verify(appointmentService).scheduleAppointment(isA(com.authenticate.Infosys_EDoctor.Entity.Appointment.class));
    }

    @Test
    @DisplayName("Test getPatientStatsById(String)")
    void testGetPatientStatsById() {
        adminServiceImpl.getPatientStatsById("42");
        verify(patientService).getPatientStatsById(eq("42"));
    }

    @Test
    @DisplayName("Test getAllPatientStats()")
    void testGetAllPatientStats() {
        adminServiceImpl.getAllPatientStats();
        verify(patientService).getAllPatientStats();
    }

    @Test
    @DisplayName("Test getAllDoctorStats()")
    void testGetAllDoctorStats() {
        adminServiceImpl.getAllDoctorStats();
        verify(doctorService).getAllDoctorStats();
    }

    @Test
    @DisplayName("Test getDoctorStatsById(String)")
    void testGetDoctorStatsById() {
        adminServiceImpl.getDoctorStatsById("42");
        verify(doctorService).getDoctorStatsById(eq("42"));
    }

    @Test
    @DisplayName("Test getWebStatsBetween(LocalDateTime, LocalDateTime)")
    void testGetWebStatsBetween() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(1);

        // Create Appointment objects with different statuses and paid states
        Appointment confirmedPaidAppointment = new Appointment();
        confirmedPaidAppointment.setStatus(Appointment.Status.Confirmed);
        confirmedPaidAppointment.setPaid(true);

        Appointment confirmedUnpaidAppointment = new Appointment();
        confirmedUnpaidAppointment.setStatus(Appointment.Status.Confirmed);
        confirmedUnpaidAppointment.setPaid(false);

        Appointment pendingAppointment = new Appointment();
        pendingAppointment.setStatus(Appointment.Status.Pending);
        pendingAppointment.setPaid(false);

        Appointment cancelledAppointment = new Appointment();
        cancelledAppointment.setStatus(Appointment.Status.Cancelled);
        cancelledAppointment.setPaid(false);

        // Create a list of mocked appointments
        List<Appointment> mockedAppointments = Arrays.asList(
                confirmedPaidAppointment,  // Paid confirmed appointment
                confirmedUnpaidAppointment, // Unpaid confirmed appointment
                pendingAppointment,  // Pending appointment
                cancelledAppointment  // Cancelled appointment
        );

        // Mock the behavior of appointmentService to return the mocked appointments
        when(appointmentService.findAppointmentsBetweenDates(eq(startDate), eq(endDate)))
                .thenReturn(mockedAppointments);

        // Act
        WebStatsBetweenDTO result = adminServiceImpl.getWebStatsBetween(startDate, endDate);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(4, result.getTotalAppointments(), "Total appointments should be 4");
        assertEquals(1, result.getPendingAppointments(), "There should be 1 pending appointment");
        assertEquals(2, result.getConfirmedAppointments(), "There should be 2 confirmed appointments");
        assertEquals(1, result.getCancelledAppointments(), "There should be 1 cancelled appointment");
        assertEquals(1, result.getPaidConfirmedAppointments(), "There should be 1 paid confirmed appointment");
        assertEquals(1, result.getUnpaidConfirmedAppointments(), "There should be 1 unpaid confirmed appointment");

        // Verify that appointmentService.findAppointmentsBetweenDates was called with the correct parameters
        verify(appointmentService).findAppointmentsBetweenDates(eq(startDate), eq(endDate));
    }


    @Test
    @DisplayName("Test getWebStats()")
    void testGetWebStats() {
        // Arrange
        // Mock data for appointmentService.getAppointmentsGroupedByDate()
        List<Object[]> groupedAppointments = Arrays.asList(
                new Object[]{"2024-12-26", Appointment.Status.Confirmed, true, 5L}, // 5 paid confirmed appointments
                new Object[]{"2024-12-26", Appointment.Status.Confirmed, false, 3L}, // 3 unpaid confirmed appointments
                new Object[]{"2024-12-26", Appointment.Status.Pending, false, 2L},    // 2 pending appointments
                new Object[]{"2024-12-27", Appointment.Status.Cancelled, false, 4L}   // 4 cancelled appointments
        );

        // Mock the service call to return the groupedAppointments data
        when(appointmentService.getAppointmentsGroupedByDate()).thenReturn(groupedAppointments);

        // Act
        List<WebStatsDTO> result = adminServiceImpl.getWebStats();

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(2, result.size(), "There should be 2 dates in the result"); // 2 distinct dates

        // Validate the WebStatsDTO for "2024-12-26"
        WebStatsDTO statsForDec26 = result.stream()
                .filter(dto -> "2024-12-26".equals(dto.getDate()))
                .findFirst()
                .orElse(null);

        assertNotNull(statsForDec26, "Stats for 2024-12-26 should be present");
        assertEquals(10, statsForDec26.getTotalAppointments(), "Total appointments for 2024-12-26 should be 10");
        assertEquals(2, statsForDec26.getPendingAppointments(), "Pending appointments for 2024-12-26 should be 2");
        assertEquals(8, statsForDec26.getConfirmedAppointments(), "Confirmed appointments for 2024-12-26 should be 8");
        assertEquals(0, statsForDec26.getCancelledAppointments(), "Cancelled appointments for 2024-12-26 should be 0");
        assertEquals(5, statsForDec26.getPaidConfirmedAppointments(), "Paid confirmed appointments for 2024-12-26 should be 5");
        assertEquals(3, statsForDec26.getUnpaidConfirmedAppointments(), "Unpaid confirmed appointments for 2024-12-26 should be 3");

        // Validate the WebStatsDTO for "2024-12-27"
        WebStatsDTO statsForDec27 = result.stream()
                .filter(dto -> "2024-12-27".equals(dto.getDate()))
                .findFirst()
                .orElse(null);

        assertNotNull(statsForDec27, "Stats for 2024-12-27 should be present");
        assertEquals(4, statsForDec27.getTotalAppointments(), "Total appointments for 2024-12-27 should be 4");
        assertEquals(0, statsForDec27.getPendingAppointments(), "Pending appointments for 2024-12-27 should be 0");
        assertEquals(0, statsForDec27.getConfirmedAppointments(), "Confirmed appointments for 2024-12-27 should be 0");
        assertEquals(4, statsForDec27.getCancelledAppointments(), "Cancelled appointments for 2024-12-27 should be 4");
        assertEquals(0, statsForDec27.getPaidConfirmedAppointments(), "Paid confirmed appointments for 2024-12-27 should be 0");
        assertEquals(0, statsForDec27.getUnpaidConfirmedAppointments(), "Unpaid confirmed appointments for 2024-12-27 should be 0");

        // Verify interaction with the appointmentService
        verify(appointmentService).getAppointmentsGroupedByDate();
    }

    @Test
    @DisplayName("Test getAdminById(String)")
    void testGetAdminById() {
        adminServiceImpl.getAdminById("42");
        verify(adminRepository).getReferenceById(eq("42"));
    }

    @Test
    @DisplayName("Test addPatient(String, Patient)")
    void testAddPatient() {
        String patientUsername = "janedoe";
        Patient patient = new Patient();
        patient.setPatientId("42");

        // Mock the userService to return a user with the role PATIENT
        User mockUser = new User();
        mockUser.setUsername(patientUsername);
        mockUser.setRole(User.Role.PATIENT);
        mockUser.setPassword("password123");
        mockUser.setEmail("janedoe@example.com");

        when(userService.getUserByUsername(patientUsername)).thenReturn(mockUser);

        // Call the addPatient method
        adminServiceImpl.addPatient(patientUsername, patient);

        // Verify that the addPatient method of patientService is called with the correct patient
        verify(patientService).addPatient(eq(patient));
    }


}