package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Repository.DoctorAvailabilityRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {DoctorAvailabilityServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class DoctorAvailabilityServiceImplTest {
    @MockBean
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Autowired
    private DoctorAvailabilityServiceImpl doctorAvailabilityServiceImpl;

    /**
     * Test
     * {@link DoctorAvailabilityServiceImpl#addAvailability(DoctorAvailability)}.
     * <ul>
     *   <li>Then return {@link DoctorAvailability} (default constructor).</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#addAvailability(DoctorAvailability)}
     */
    @Test
    @DisplayName("Test addAvailability(DoctorAvailability); then return DoctorAvailability (default constructor)")
    void testAddAvailability_thenReturnDoctorAvailability() {
        // Arrange
        DoctorAvailability doctorAvailability = new DoctorAvailability();
        doctorAvailability.setAvailabilityId(1);
        doctorAvailability.setDoctorId("42");
        doctorAvailability.setEndDate(LocalDate.of(1970, 1, 1));
        doctorAvailability.setFromDate(LocalDate.of(1970, 1, 1));
        when(doctorAvailabilityRepository.save(Mockito.<DoctorAvailability>any())).thenReturn(doctorAvailability);

        DoctorAvailability availability = new DoctorAvailability();
        availability.setAvailabilityId(1);
        availability.setDoctorId("42");
        availability.setEndDate(LocalDate.of(1970, 1, 1));
        availability.setFromDate(LocalDate.of(1970, 1, 1));

        // Act
        DoctorAvailability actualAddAvailabilityResult = doctorAvailabilityServiceImpl.addAvailability(availability);

        // Assert
        verify(doctorAvailabilityRepository).save(isA(DoctorAvailability.class));
        assertSame(doctorAvailability, actualAddAvailabilityResult);
    }

    /**
     * Test
     * {@link DoctorAvailabilityServiceImpl#addAvailability(DoctorAvailability)}.
     * <ul>
     *   <li>Then throw {@link RuntimeException}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#addAvailability(DoctorAvailability)}
     */
    @Test
    @DisplayName("Test addAvailability(DoctorAvailability); then throw RuntimeException")
    void testAddAvailability_thenThrowRuntimeException() {
        // Arrange
        when(doctorAvailabilityRepository.save(Mockito.<DoctorAvailability>any())).thenThrow(new RuntimeException("foo"));

        DoctorAvailability availability = new DoctorAvailability();
        availability.setAvailabilityId(1);
        availability.setDoctorId("42");
        availability.setEndDate(LocalDate.of(1970, 1, 1));
        availability.setFromDate(LocalDate.of(1970, 1, 1));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> doctorAvailabilityServiceImpl.addAvailability(availability));
        verify(doctorAvailabilityRepository).save(isA(DoctorAvailability.class));
    }

    /**
     * Test {@link DoctorAvailabilityServiceImpl#getAvailabilityByDoctorId(String)}.
     * <ul>
     *   <li>Then return Empty.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#getAvailabilityByDoctorId(String)}
     */
    @Test
    @DisplayName("Test getAvailabilityByDoctorId(String); then return Empty")
    void testGetAvailabilityByDoctorId_thenReturnEmpty() {
        // Arrange
        when(doctorAvailabilityRepository.findByDoctorId(Mockito.<String>any())).thenReturn(new ArrayList<>());

        // Act
        List<DoctorAvailability> actualAvailabilityByDoctorId = doctorAvailabilityServiceImpl
                .getAvailabilityByDoctorId("42");

        // Assert
        verify(doctorAvailabilityRepository).findByDoctorId(eq("42"));
        assertTrue(actualAvailabilityByDoctorId.isEmpty());
    }

    /**
     * Test {@link DoctorAvailabilityServiceImpl#getAvailabilityByDoctorId(String)}.
     * <ul>
     *   <li>Then throw {@link RuntimeException}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#getAvailabilityByDoctorId(String)}
     */
    @Test
    @DisplayName("Test getAvailabilityByDoctorId(String); then throw RuntimeException")
    void testGetAvailabilityByDoctorId_thenThrowRuntimeException() {
        // Arrange
        when(doctorAvailabilityRepository.findByDoctorId(Mockito.<String>any())).thenThrow(new RuntimeException("foo"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> doctorAvailabilityServiceImpl.getAvailabilityByDoctorId("42"));
        verify(doctorAvailabilityRepository).findByDoctorId(eq("42"));
    }

    /**
     * Test
     * {@link DoctorAvailabilityServiceImpl#updateAvailability(int, DoctorAvailability)}.
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#updateAvailability(int, DoctorAvailability)}
     */
    @Test
    @DisplayName("Test updateAvailability(int, DoctorAvailability)")
    void testUpdateAvailability() {
        // Arrange
        DoctorAvailability doctorAvailability = new DoctorAvailability();
        doctorAvailability.setAvailabilityId(1);
        doctorAvailability.setDoctorId("42");
        doctorAvailability.setEndDate(LocalDate.of(1970, 1, 1));
        doctorAvailability.setFromDate(LocalDate.of(1970, 1, 1));
        Optional<DoctorAvailability> ofResult = Optional.of(doctorAvailability);
        when(doctorAvailabilityRepository.save(Mockito.<DoctorAvailability>any())).thenThrow(new RuntimeException("foo"));
        when(doctorAvailabilityRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        DoctorAvailability updatedAvailability = new DoctorAvailability();
        updatedAvailability.setAvailabilityId(1);
        updatedAvailability.setDoctorId("42");
        updatedAvailability.setEndDate(LocalDate.of(1970, 1, 1));
        updatedAvailability.setFromDate(LocalDate.of(1970, 1, 1));

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> doctorAvailabilityServiceImpl.updateAvailability(1, updatedAvailability));
        verify(doctorAvailabilityRepository).findById(eq(1));
        verify(doctorAvailabilityRepository).save(isA(DoctorAvailability.class));
    }

    /**
     * Test
     * {@link DoctorAvailabilityServiceImpl#updateAvailability(int, DoctorAvailability)}.
     * <ul>
     *   <li>Given {@link DoctorAvailabilityRepository}
     * {@link CrudRepository#findById(Object)} return empty.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#updateAvailability(int, DoctorAvailability)}
     */
    @Test
    @DisplayName("Test updateAvailability(int, DoctorAvailability); given DoctorAvailabilityRepository findById(Object) return empty")
    void testUpdateAvailability_givenDoctorAvailabilityRepositoryFindByIdReturnEmpty() {
        // Arrange
        Optional<DoctorAvailability> emptyResult = Optional.empty();
        when(doctorAvailabilityRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        DoctorAvailability updatedAvailability = new DoctorAvailability();
        updatedAvailability.setAvailabilityId(1);
        updatedAvailability.setDoctorId("42");
        updatedAvailability.setEndDate(LocalDate.of(1970, 1, 1));
        updatedAvailability.setFromDate(LocalDate.of(1970, 1, 1));

        // Act and Assert
        assertThrows(RuntimeException.class,
                () -> doctorAvailabilityServiceImpl.updateAvailability(1, updatedAvailability));
        verify(doctorAvailabilityRepository).findById(eq(1));
    }

    /**
     * Test
     * {@link DoctorAvailabilityServiceImpl#updateAvailability(int, DoctorAvailability)}.
     * <ul>
     *   <li>Then return {@link DoctorAvailability} (default constructor).</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#updateAvailability(int, DoctorAvailability)}
     */
    @Test
    @DisplayName("Test updateAvailability(int, DoctorAvailability); then return DoctorAvailability (default constructor)")
    void testUpdateAvailability_thenReturnDoctorAvailability() {
        // Arrange
        DoctorAvailability doctorAvailability = new DoctorAvailability();
        doctorAvailability.setAvailabilityId(1);
        doctorAvailability.setDoctorId("42");
        doctorAvailability.setEndDate(LocalDate.of(1970, 1, 1));
        doctorAvailability.setFromDate(LocalDate.of(1970, 1, 1));
        Optional<DoctorAvailability> ofResult = Optional.of(doctorAvailability);

        DoctorAvailability doctorAvailability2 = new DoctorAvailability();
        doctorAvailability2.setAvailabilityId(1);
        doctorAvailability2.setDoctorId("42");
        doctorAvailability2.setEndDate(LocalDate.of(1970, 1, 1));
        doctorAvailability2.setFromDate(LocalDate.of(1970, 1, 1));
        when(doctorAvailabilityRepository.save(Mockito.<DoctorAvailability>any())).thenReturn(doctorAvailability2);
        when(doctorAvailabilityRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        DoctorAvailability updatedAvailability = new DoctorAvailability();
        updatedAvailability.setAvailabilityId(1);
        updatedAvailability.setDoctorId("42");
        updatedAvailability.setEndDate(LocalDate.of(1970, 1, 1));
        updatedAvailability.setFromDate(LocalDate.of(1970, 1, 1));

        // Act
        DoctorAvailability actualUpdateAvailabilityResult = doctorAvailabilityServiceImpl.updateAvailability(1,
                updatedAvailability);

        // Assert
        verify(doctorAvailabilityRepository).findById(eq(1));
        verify(doctorAvailabilityRepository).save(isA(DoctorAvailability.class));
        assertSame(doctorAvailability2, actualUpdateAvailabilityResult);
    }

    /**
     * Test {@link DoctorAvailabilityServiceImpl#deleteAvailability(int)}.
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#deleteAvailability(int)}
     */
    @Test
    @DisplayName("Test deleteAvailability(int)")
    void testDeleteAvailability() {
        // Arrange
        DoctorAvailability doctorAvailability = new DoctorAvailability();
        doctorAvailability.setAvailabilityId(1);
        doctorAvailability.setDoctorId("42");
        doctorAvailability.setEndDate(LocalDate.of(1970, 1, 1));
        doctorAvailability.setFromDate(LocalDate.of(1970, 1, 1));
        Optional<DoctorAvailability> ofResult = Optional.of(doctorAvailability);
        doThrow(new RuntimeException("foo")).when(doctorAvailabilityRepository).deleteById(Mockito.<Integer>any());
        when(doctorAvailabilityRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> doctorAvailabilityServiceImpl.deleteAvailability(1));
        verify(doctorAvailabilityRepository).deleteById(eq(1));
        verify(doctorAvailabilityRepository).findById(eq(1));
    }

    /**
     * Test {@link DoctorAvailabilityServiceImpl#deleteAvailability(int)}.
     * <ul>
     *   <li>Given {@link DoctorAvailabilityRepository}
     * {@link CrudRepository#findById(Object)} return empty.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#deleteAvailability(int)}
     */
    @Test
    @DisplayName("Test deleteAvailability(int); given DoctorAvailabilityRepository findById(Object) return empty")
    void testDeleteAvailability_givenDoctorAvailabilityRepositoryFindByIdReturnEmpty() {
        // Arrange
        Optional<DoctorAvailability> emptyResult = Optional.empty();
        when(doctorAvailabilityRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> doctorAvailabilityServiceImpl.deleteAvailability(1));
        verify(doctorAvailabilityRepository).findById(eq(1));
    }

    /**
     * Test {@link DoctorAvailabilityServiceImpl#deleteAvailability(int)}.
     * <ul>
     *   <li>Then return {@code true}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link DoctorAvailabilityServiceImpl#deleteAvailability(int)}
     */
    @Test
    @DisplayName("Test deleteAvailability(int); then return 'true'")
    void testDeleteAvailability_thenReturnTrue() {
        // Arrange
        DoctorAvailability doctorAvailability = new DoctorAvailability();
        doctorAvailability.setAvailabilityId(1);
        doctorAvailability.setDoctorId("42");
        doctorAvailability.setEndDate(LocalDate.of(1970, 1, 1));
        doctorAvailability.setFromDate(LocalDate.of(1970, 1, 1));
        Optional<DoctorAvailability> ofResult = Optional.of(doctorAvailability);
        doNothing().when(doctorAvailabilityRepository).deleteById(Mockito.<Integer>any());
        when(doctorAvailabilityRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        boolean actualDeleteAvailabilityResult = doctorAvailabilityServiceImpl.deleteAvailability(1);

        // Assert
        verify(doctorAvailabilityRepository).deleteById(eq(1));
        verify(doctorAvailabilityRepository).findById(eq(1));
        assertTrue(actualDeleteAvailabilityResult);
    }
}
