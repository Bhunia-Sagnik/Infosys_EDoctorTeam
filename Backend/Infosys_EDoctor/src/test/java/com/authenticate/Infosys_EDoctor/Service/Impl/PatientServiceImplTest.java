package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.DTO.AppointmentRequest;
import com.authenticate.Infosys_EDoctor.DTO.PatientStatsDTO;
import com.authenticate.Infosys_EDoctor.Entity.*;
import com.authenticate.Infosys_EDoctor.Repository.PatientRepository;
import com.authenticate.Infosys_EDoctor.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserService userService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private DoctorAvailabilityService doctorAvailabilityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPatient_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setEmail("test@example.com");

        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.empty());
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act
        Patient savedPatient = patientService.addPatient(patient);

        // Assert
        assertNotNull(savedPatient);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testAddPatient_EmailAlreadyExists() {
        // Arrange
        Patient patient = new Patient();
        patient.setEmail("test@example.com");

        when(patientRepository.findByEmail(patient.getEmail())).thenReturn(Optional.of(patient));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> patientService.addPatient(patient));
        assertEquals("Patient with given email already exists", exception.getMessage());
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void testUpdateProfile_Success() {
        // Arrange
        String patientId = "PAT-1234";
        Patient existingPatient = new Patient();
        existingPatient.setPatientId(patientId);
        existingPatient.setName("Old Name");

        Patient updatedPatient = new Patient();
        updatedPatient.setName("New Name");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);

        // Act
        Patient result = patientService.updateProfile(patientId, updatedPatient);

        // Assert
        assertEquals("New Name", result.getName());
        verify(patientRepository, times(1)).save(existingPatient);
    }

    @Test
    void testUpdateProfile_PatientNotFound() {
        // Arrange
        String patientId = "PAT-1234";
        Patient updatedPatient = new Patient();

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.updateProfile(patientId, updatedPatient));
        assertEquals("Patient not found with ID: " + patientId, exception.getMessage());
    }

    @Test
    void testDeletePatient_Success() {
        // Arrange
        String patientId = "PAT-1234";
        Patient existingPatient = new Patient();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
        doNothing().when(patientRepository).delete(existingPatient);

        // Act
        patientService.deletePatient(patientId);

        // Assert
        verify(patientRepository, times(1)).delete(existingPatient);
    }

    @Test
    void testDeletePatient_PatientNotFound() {
        // Arrange
        String patientId = "PAT-1234";

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> patientService.deletePatient(patientId));
        assertEquals("Patient not found with ID: " + patientId, exception.getMessage());
    }

    @Test
    void testFindDoctors_Success() {
        // Arrange
        List<Doctor> doctors = Arrays.asList(new Doctor(), new Doctor());
        when(doctorService.findAllDoctors()).thenReturn(doctors);

        // Act
        List<Doctor> result = patientService.findDoctors();

        // Assert
        assertEquals(2, result.size());
        verify(doctorService, times(1)).findAllDoctors();
    }

    @Test
    void testMakeAppointment_Success() {
        // Arrange
        Appointment appointment = new Appointment();

        when(appointmentService.scheduleAppointment(appointment)).thenReturn(appointment);

        // Act
        Appointment result = patientService.makeAppointment(appointment);

        // Assert
        assertNotNull(result);
        verify(appointmentService, times(1)).scheduleAppointment(appointment);
    }
    @Test
    void testFindDoctorsBySpecialization_Success() {
        // Arrange
        String specialization = "Cardiology";
        List<Doctor> doctors = Arrays.asList(new Doctor(), new Doctor());
        when(doctorService.findDoctorsBySpecialization(specialization)).thenReturn(doctors);

        // Act
        List<Doctor> result = patientService.findDoctorsBySpecialization(specialization);

        // Assert
        assertEquals(2, result.size());
        verify(doctorService, times(1)).findDoctorsBySpecialization(specialization);
    }

    @Test
    void testGetAvailableDates_Success() {
        // Arrange
        String doctorId = "DOC-123";
        List<DoctorAvailability> availabilities = Arrays.asList(new DoctorAvailability(), new DoctorAvailability());
        when(doctorAvailabilityService.getAvailabilityByDoctorId(doctorId)).thenReturn(availabilities);

        // Act
        List<DoctorAvailability> result = patientService.getAvailableDates(doctorId);

        // Assert
        assertEquals(2, result.size());
        verify(doctorAvailabilityService, times(1)).getAvailabilityByDoctorId(doctorId);
    }

    @Test
    void testGetPatientById_Success() {
        // Arrange
        String patientId = "PAT-123";
        Patient patient = new Patient();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // Act
        Patient result = patientService.getPatientById(patientId);

        // Assert
        assertNotNull(result);
        verify(patientRepository, times(1)).findById(patientId);
    }

    @Test
    void testGetPatientById_PatientNotFound() {
        // Arrange
        String patientId = "PAT-123";
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> patientService.getPatientById(patientId));
        assertEquals("Patient with " + patientId + " not found", exception.getMessage());
    }

    @Test
    void testGetPatientByUserId_Success() {
        // Arrange
        Long userId = 1L;
        Patient patient = new Patient();
        User user = new User();
        user.setEmail("test@example.com");
        when(userService.getUserById(userId)).thenReturn(user);
        when(patientRepository.findByEmail("test@example.com")).thenReturn(Optional.of(patient));

        // Act
        Patient result = patientService.getPatientByUserId(userId);

        // Assert
        assertNotNull(result);
        verify(patientRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testViewAppointments_Success() {
        // Arrange
        String patientId = "PAT-123";
        List<Appointment> appointments = Arrays.asList(new Appointment(), new Appointment());
        when(appointmentService.getAppointmentsForPatient(patientId)).thenReturn(appointments);

        // Act
        List<Appointment> result = patientService.viewAppointments(patientId);

        // Assert
        assertEquals(2, result.size());
        verify(appointmentService, times(1)).getAppointmentsForPatient(patientId);
    }

    @Test
    void testGetPatientByEmail_Success() {
        // Arrange
        String email = "test@example.com";
        Patient patient = new Patient();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));

        // Act
        Optional<Patient> result = patientService.getPatientByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        verify(patientRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindDoctorsByName_Success() {
        // Arrange
        String doctorName = "Dr. John";
        List<Doctor> doctors = Arrays.asList(new Doctor(), new Doctor());
        when(doctorService.getDoctorsByName(doctorName)).thenReturn(doctors);

        // Act
        List<Doctor> result = patientService.findDoctorsByName(doctorName);

        // Assert
        assertEquals(2, result.size());
        verify(doctorService, times(1)).getDoctorsByName(doctorName);
    }

    @Test
    void testGetDoctorById_Success() {
        // Arrange
        String doctorId = "DOC-123";
        Doctor doctor = new Doctor();
        when(doctorService.getDoctorById(doctorId)).thenReturn(doctor);

        // Act
        Doctor result = patientService.getDoctorById(doctorId);

        // Assert
        assertNotNull(result);
        verify(doctorService, times(1)).getDoctorById(doctorId);
    }

    @Test
    void testViewConfirmedAppointments_Success() {
        // Arrange
        String patientId = "PAT-123";
        Appointment confirmedAppointment = new Appointment();
        confirmedAppointment.setStatus(Appointment.Status.Confirmed);
        List<Appointment> appointments = Arrays.asList(confirmedAppointment, new Appointment());
        when(appointmentService.getAppointmentsForPatient(patientId)).thenReturn(appointments);

        // Act
        List<Appointment> result = patientService.viewConfirmedAppointments(patientId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Appointment.Status.Confirmed, result.get(0).getStatus());
        verify(appointmentService, times(1)).getAppointmentsForPatient(patientId);
    }

    @Test
    void testGetAllPatients_Success() {
        // Arrange
        List<Patient> patients = Arrays.asList(new Patient(), new Patient());
        when(patientRepository.findAll()).thenReturn(patients);

        // Act
        List<Patient> result = patientService.getAllPatients();

        // Assert
        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void testGetAllPatientStats_Success() {
        // Arrange
        Patient patient = new Patient();
        patient.setPatientId("PAT-123");
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setMobileNo("1234567890");

        Appointment confirmedAppointment = new Appointment();
        confirmedAppointment.setStatus(Appointment.Status.Confirmed);
        confirmedAppointment.setPaid(true);

        List<Appointment> appointments = Arrays.asList(confirmedAppointment);
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient));
        when(appointmentService.getAppointmentsForPatient("PAT-123")).thenReturn(appointments);

        // Act
        List<PatientStatsDTO> result = patientService.getAllPatientStats();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getTotalAppointments());
        assertEquals(1, result.get(0).getConfirmedAppointments());
        assertEquals(1, result.get(0).getPaidAppointments());
    }

    @Test
    void testGetPatientStatsById_Success() {
        // Arrange
        String patientId = "PAT-123";
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setMobileNo("1234567890");

        Appointment confirmedAppointment = new Appointment();
        confirmedAppointment.setStatus(Appointment.Status.Confirmed);
        confirmedAppointment.setPaid(true);

        List<Appointment> appointments = Arrays.asList(confirmedAppointment);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(appointmentService.getAppointmentsForPatient(patientId)).thenReturn(appointments);

        // Act
        PatientStatsDTO result = patientService.getPatientStatsById(patientId);

        // Assert
        assertNotNull(result);
        assertEquals(patientId, result.getPatientId());
        assertEquals(1, result.getTotalAppointments());
        assertEquals(1, result.getConfirmedAppointments());
        assertEquals(1, result.getPaidAppointments());
    }

}