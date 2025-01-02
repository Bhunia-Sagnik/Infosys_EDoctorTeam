package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Repository.DoctorAvailabilityRepository;
import com.authenticate.Infosys_EDoctor.Repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorAvailabilityServiceImplTest {

    @Mock
    private DoctorAvailabilityRepository availabilityRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorAvailabilityServiceImpl availabilityService;

    private Doctor doctor;
    private DoctorAvailability availability;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setDoctorId("42");
        doctor.setName("Dr. John");

        availability = new DoctorAvailability();
        availability.setDoctorId("42");
        availability.setFromDate(LocalDate.now());
        availability.setEndDate(LocalDate.now().plusDays(7));
    }

    @Test
    @DisplayName("Test addAvailability; when doctor exists, save availability")
    void testAddAvailability_DoctorExists() {
        // Arrange
        when(doctorRepository.findByDoctorId("42")).thenReturn(Optional.of(doctor));
        when(availabilityRepository.save(availability)).thenReturn(availability);

        // Act
        DoctorAvailability savedAvailability = availabilityService.addAvailability(availability);

        // Assert
        assertNotNull(savedAvailability);
        assertEquals(availability, savedAvailability);
        verify(doctorRepository).findByDoctorId("42");
        verify(availabilityRepository).save(availability);
    }

    @Test
    @DisplayName("Test addAvailability; when doctor doesn't exist, throw exception")
    void testAddAvailability_DoctorDoesNotExist() {
        // Arrange
        when(doctorRepository.findByDoctorId("42")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> availabilityService.addAvailability(availability));
        assertEquals("Add doctor first", exception.getMessage());
        verify(doctorRepository).findByDoctorId("42");
        verify(availabilityRepository, times(0)).save(availability);
    }

    @Test
    @DisplayName("Test getAvailabilityByDoctorId; return list of availabilities")
    void testGetAvailabilityByDoctorId() {
        // Arrange
        when(availabilityRepository.findByDoctorId("42")).thenReturn(List.of(availability));

        // Act
        List<DoctorAvailability> availabilities = availabilityService.getAvailabilityByDoctorId("42");

        // Assert
        assertNotNull(availabilities);
        assertEquals(1, availabilities.size());
        assertEquals(availability, availabilities.get(0));
        verify(availabilityRepository).findByDoctorId("42");
    }

    @Test
    @DisplayName("Test updateAvailability; when availability exists, update it")
    void testUpdateAvailability() {
        // Arrange
        DoctorAvailability updatedAvailability = new DoctorAvailability();
        updatedAvailability.setDoctorId("42");
        updatedAvailability.setFromDate(LocalDate.now().plusDays(1));
        updatedAvailability.setEndDate(LocalDate.now().plusDays(8));

        when(availabilityRepository.findById(1)).thenReturn(Optional.of(availability));
        when(availabilityRepository.save(updatedAvailability)).thenReturn(updatedAvailability);

        // Act
        DoctorAvailability result = availabilityService.updateAvailability(1, updatedAvailability);

        // Assert
        assertNotNull(result);
        assertEquals(updatedAvailability, result);
        verify(availabilityRepository).findById(1);
        verify(availabilityRepository).save(updatedAvailability);
    }

    @Test
    @DisplayName("Test updateAvailability; when availability not found, throw exception")
    void testUpdateAvailability_NotFound() {
        // Arrange
        DoctorAvailability updatedAvailability = new DoctorAvailability();
        updatedAvailability.setDoctorId("42");
        updatedAvailability.setFromDate(LocalDate.now().plusDays(1));
        updatedAvailability.setEndDate(LocalDate.now().plusDays(8));

        when(availabilityRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> availabilityService.updateAvailability(1, updatedAvailability));
        assertEquals("Availability not found", exception.getMessage());
        verify(availabilityRepository).findById(1);
    }

    @Test
    @DisplayName("Test deleteAvailability; when availability exists, delete it")
    void testDeleteAvailability() {
        // Arrange
        when(availabilityRepository.findById(1)).thenReturn(Optional.of(availability));

        // Act
        boolean result = availabilityService.deleteAvailability(1);

        // Assert
        assertTrue(result);
        verify(availabilityRepository).findById(1);
        verify(availabilityRepository).deleteById(1);
    }

    @Test
    @DisplayName("Test deleteAvailability; when availability not found, throw exception")
    void testDeleteAvailability_NotFound() {
        // Arrange
        when(availabilityRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> availabilityService.deleteAvailability(1));
        assertEquals("Availability not found", exception.getMessage());
        verify(availabilityRepository).findById(1);
        verify(availabilityRepository, times(0)).deleteById(1);
    }
}