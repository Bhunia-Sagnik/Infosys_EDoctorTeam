package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.DTO.DoctorStatsDTO;
import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Repository.DoctorRepository;
import com.authenticate.Infosys_EDoctor.Service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private DoctorAvailabilityService doctorAvailabilityService;

    @InjectMocks
    private DoctorServiceImpl doctorServiceImpl;

    @Test
    @DisplayName("Add doctor successfully")
    void addDoctor_success() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setEmail("test@example.com");

        when(doctorRepository.findByEmail("test@example.com")).thenReturn(null);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        // Act
        Doctor result = doctorServiceImpl.addDoctor(doctor);

        // Assert
        assertNotNull(result);
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Add doctor throws exception when email already exists")
    void addDoctor_emailExists_throwsException() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setEmail("test@example.com");

        when(doctorRepository.findByEmail("test@example.com")).thenReturn(doctor);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> doctorServiceImpl.addDoctor(doctor));
    }

    @Test
    @DisplayName("Get doctor by ID successfully")
    void getDoctorById_success() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId("DOC123");
        when(doctorRepository.findByDoctorId("DOC123")).thenReturn(Optional.of(doctor));

        // Act
        Doctor result = doctorServiceImpl.getDoctorById("DOC123");

        // Assert
        assertNotNull(result);
        assertEquals("DOC123", result.getDoctorId());
    }

    @Test
    @DisplayName("Get doctor by ID throws exception when not found")
    void getDoctorById_notFound_throwsException() {
        // Arrange
        when(doctorRepository.findByDoctorId("DOC123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> doctorServiceImpl.getDoctorById("DOC123"));
    }

    @Test
    @DisplayName("Update doctor successfully")
    void updateDoctor_success() {
        // Arrange
        Doctor existingDoctor = new Doctor();
        existingDoctor.setDoctorId("DOC123");
        existingDoctor.setName("Old Name");

        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setName("New Name");

        when(doctorRepository.findByDoctorId("DOC123")).thenReturn(Optional.of(existingDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(existingDoctor);

        // Act
        Doctor result = doctorServiceImpl.updateDoctor("DOC123", updatedDoctor);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getName());
    }

    @Test
    @DisplayName("Delete doctor successfully")
    void deleteDoctor_success() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId("DOC123");
        when(doctorRepository.findByDoctorId("DOC123")).thenReturn(Optional.of(doctor));
        doNothing().when(doctorRepository).delete(any(Doctor.class));

        // Act
        doctorServiceImpl.deleteDoctor("DOC123");

        // Assert
        verify(doctorRepository).delete(doctor);
    }

    @Test
    @DisplayName("Find doctors by specialization")
    void findDoctorsBySpecialization_success() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setSpecialization("Cardiology");
        List<Doctor> doctors = List.of(doctor);

        when(doctorRepository.findAll()).thenReturn(doctors);

        // Act
        List<Doctor> result = doctorServiceImpl.findDoctorsBySpecialization("Cardiology");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Get all doctor stats")
    void getAllDoctorStats_success() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId("DOC123");
        List<Doctor> doctors = List.of(doctor);

        Appointment appointment = new Appointment();
        appointment.setStatus(Appointment.Status.Confirmed);
        appointment.setPaid(true);

        when(doctorRepository.findAll()).thenReturn(doctors);
        when(appointmentService.getAppointmentsForDoctor("DOC123")).thenReturn(List.of(appointment));

        // Act
        List<DoctorStatsDTO> result = doctorServiceImpl.getAllDoctorStats();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getTotalAppointments());
    }
}