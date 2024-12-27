package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Repository.AppointmentRepository;
import com.authenticate.Infosys_EDoctor.Repository.DoctorAvailabilityRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

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

@ContextConfiguration(classes = {AppointmentServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AppointmentServiceImplTest {
    @MockBean
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentServiceImpl appointmentServiceImpl;

    @MockBean
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    /**
     * Test {@link AppointmentServiceImpl#getAppointmentById(Long)}.
     * <ul>
     *   <li>Given {@link Doctor} (default constructor) ChargedPerVisit is one.</li>
     *   <li>Then return {@link Appointment} (default constructor).</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#getAppointmentById(Long)}
     */
    @Test
    @DisplayName("Test getAppointmentById(Long); given Doctor (default constructor) ChargedPerVisit is one; then return Appointment (default constructor)")
    void testGetAppointmentById_givenDoctorChargedPerVisitIsOne_thenReturnAppointment() {
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
        appointment.setPatient(patient);
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);
        Optional<Appointment> ofResult = Optional.of(appointment);
        when(appointmentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        Appointment actualAppointmentById = appointmentServiceImpl.getAppointmentById(1L);

        // Assert
        verify(appointmentRepository).findById(eq(1L));
        assertSame(appointment, actualAppointmentById);
    }

    /**
     * Test {@link AppointmentServiceImpl#getAppointmentById(Long)}.
     * <ul>
     *   <li>Then throw {@link IllegalArgumentException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#getAppointmentById(Long)}
     */
    @Test
    @DisplayName("Test getAppointmentById(Long); then throw IllegalArgumentException")
    void testGetAppointmentById_thenThrowIllegalArgumentException() {
        // Arrange
        Optional<Appointment> emptyResult = Optional.empty();
        when(appointmentRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> appointmentServiceImpl.getAppointmentById(1L));
        verify(appointmentRepository).findById(eq(1L));
    }

    /**
     * Test {@link AppointmentServiceImpl#getAppointmentById(Long)}.
     * <ul>
     *   <li>Then throw {@link RuntimeException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#getAppointmentById(Long)}
     */
    @Test
    @DisplayName("Test getAppointmentById(Long); then throw RuntimeException")
    void testGetAppointmentById_thenThrowRuntimeException() {
        // Arrange
        when(appointmentRepository.findById(Mockito.<Long>any())).thenThrow(new RuntimeException("foo"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> appointmentServiceImpl.getAppointmentById(1L));
        verify(appointmentRepository).findById(eq(1L));
    }

    /**
     * Test {@link AppointmentServiceImpl#cancelAppointment(Long, String)}.
     * <ul>
     *   <li>Given {@link AppointmentRepository} {@link CrudRepository#save(Object)}
     * return {@link Appointment} (default constructor).</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link AppointmentServiceImpl#cancelAppointment(Long, String)}
     */
    @Test
    @DisplayName("Test cancelAppointment(Long, String); given AppointmentRepository save(Object) return Appointment (default constructor)")
    void testCancelAppointment_givenAppointmentRepositorySaveReturnAppointment() {
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
        appointment.setPatient(patient);
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);
        Optional<Appointment> ofResult = Optional.of(appointment);

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

        Appointment appointment2 = new Appointment();
        appointment2.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        appointment2.setAppointmentId(1L);
        appointment2.setDoctor(doctor2);
        appointment2.setPatient(patient2);
        appointment2.setReason("Just cause");
        appointment2.setStatus(Appointment.Status.Pending);
        when(appointmentRepository.save(Mockito.<Appointment>any())).thenReturn(appointment2);
        when(appointmentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        appointmentServiceImpl.cancelAppointment(1L, "Just cause");

        // Assert
        verify(appointmentRepository).findById(eq(1L));
        verify(appointmentRepository).save(isA(Appointment.class));
    }

    /**
     * Test {@link AppointmentServiceImpl#cancelAppointment(Long, String)}.
     * <ul>
     *   <li>Then throw {@link RuntimeException}.</li>
     * </ul>
     * <p>
     * Method under test:
     * {@link AppointmentServiceImpl#cancelAppointment(Long, String)}
     */
    @Test
    @DisplayName("Test cancelAppointment(Long, String); then throw RuntimeException")
    void testCancelAppointment_thenThrowRuntimeException() {
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
        appointment.setPatient(patient);
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);
        Optional<Appointment> ofResult = Optional.of(appointment);
        when(appointmentRepository.save(Mockito.<Appointment>any())).thenThrow(new RuntimeException("foo"));
        when(appointmentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> appointmentServiceImpl.cancelAppointment(1L, "Just cause"));
        verify(appointmentRepository).findById(eq(1L));
        verify(appointmentRepository).save(isA(Appointment.class));
    }

    /**
     * Test {@link AppointmentServiceImpl#confirmAppointment(Long)}.
     * <ul>
     *   <li>Then return {@link Appointment} (default constructor).</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#confirmAppointment(Long)}
     */
    @Test
    @DisplayName("Test confirmAppointment(Long); then return Appointment (default constructor)")
    void testConfirmAppointment_thenReturnAppointment() {
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
        appointment.setPatient(patient);
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);
        Optional<Appointment> ofResult = Optional.of(appointment);

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

        Appointment appointment2 = new Appointment();
        appointment2.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        appointment2.setAppointmentId(1L);
        appointment2.setDoctor(doctor2);
        appointment2.setPatient(patient2);
        appointment2.setReason("Just cause");
        appointment2.setStatus(Appointment.Status.Pending);
        when(appointmentRepository.save(Mockito.<Appointment>any())).thenReturn(appointment2);
        when(appointmentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        Appointment actualConfirmAppointmentResult = appointmentServiceImpl.confirmAppointment(1L);

        // Assert
        verify(appointmentRepository).findById(eq(1L));
        verify(appointmentRepository).save(isA(Appointment.class));
        assertSame(appointment2, actualConfirmAppointmentResult);
    }

    /**
     * Test {@link AppointmentServiceImpl#confirmAppointment(Long)}.
     * <ul>
     *   <li>Then throw {@link RuntimeException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#confirmAppointment(Long)}
     */
    @Test
    @DisplayName("Test confirmAppointment(Long); then throw RuntimeException")
    void testConfirmAppointment_thenThrowRuntimeException() {
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
        appointment.setPatient(patient);
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);
        Optional<Appointment> ofResult = Optional.of(appointment);
        when(appointmentRepository.save(Mockito.<Appointment>any())).thenThrow(new RuntimeException("foo"));
        when(appointmentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> appointmentServiceImpl.confirmAppointment(1L));
        verify(appointmentRepository).findById(eq(1L));
        verify(appointmentRepository).save(isA(Appointment.class));
    }

    /**
     * Test {@link AppointmentServiceImpl#confirmAppointment(Long)}.
     * <ul>
     *   <li>Given an Appointment that exists, then confirm the appointment and return the updated Appointment.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#confirmAppointment(Long)}
     */
    @Test
    @DisplayName("Test confirmAppointment(Long); given Appointment exists, then return confirmed Appointment")
    void testConfirmAppointment_givenAppointmentExists_thenReturnConfirmedAppointment() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId("42");
        doctor.setName("Dr. Jane Doe");

        Patient patient = new Patient();
        patient.setPatientId("42");
        patient.setName("John Doe");

        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStatus(Appointment.Status.Pending);

        Optional<Appointment> ofResult = Optional.of(appointment);
        when(appointmentRepository.findById(Mockito.anyLong())).thenReturn(ofResult);
        when(appointmentRepository.save(Mockito.any(Appointment.class))).thenReturn(appointment);

        // Act
        Appointment confirmedAppointment = appointmentServiceImpl.confirmAppointment(1L);

        // Assert
        verify(appointmentRepository).findById(eq(1L));
        verify(appointmentRepository).save(isA(Appointment.class));
        assertSame(Appointment.Status.Confirmed, confirmedAppointment.getStatus());
    }

    /**
     * Test {@link AppointmentServiceImpl#scheduleAppointment(Appointment)}.
     * <ul>
     *   <li>Given doctor availability exists for the selected time slot, the appointment is scheduled successfully.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#scheduleAppointment(Appointment)}
     */
    @Test
    @DisplayName("Test scheduleAppointment(Appointment); given valid doctor availability, then schedule appointment successfully")
    void testScheduleAppointment_givenValidAvailability_thenScheduleAppointment() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId("42");
        doctor.setName("Dr. Jane Doe");

        Patient patient = new Patient();
        patient.setPatientId("42");
        patient.setName("John Doe");

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDate.of(2024, 12, 5).atStartOfDay());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        DoctorAvailability availability = new DoctorAvailability();
        availability.setFromDate(LocalDate.of(2024, 12, 1));
        availability.setEndDate(LocalDate.of(2024, 12, 10));

        when(doctorAvailabilityRepository.findByDoctorId(Mockito.anyString()))
                .thenReturn(List.of(availability));
        when(appointmentRepository.save(Mockito.any(Appointment.class)))
                .thenReturn(appointment);

        // Act
        Appointment scheduledAppointment = appointmentServiceImpl.scheduleAppointment(appointment);

        // Assert
        verify(doctorAvailabilityRepository).findByDoctorId(eq("42"));
        verify(appointmentRepository).save(isA(Appointment.class));
        assertSame(appointment, scheduledAppointment);
    }

    /**
     * Test {@link AppointmentServiceImpl#scheduleAppointment(Appointment)}.
     * <ul>
     *   <li>Then throw {@link RuntimeException} if doctor is not available for the selected time slot.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#scheduleAppointment(Appointment)}
     */
    @Test
    @DisplayName("Test scheduleAppointment(Appointment); then throw RuntimeException if doctor is not available")
    void testScheduleAppointment_thenThrowRuntimeException() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId("42");
        doctor.setName("Dr. Jane Doe");

        Patient patient = new Patient();
        patient.setPatientId("42");
        patient.setName("John Doe");

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDate.of(2024, 12, 20).atStartOfDay());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        DoctorAvailability availability = new DoctorAvailability();
        availability.setFromDate(LocalDate.of(2024, 12, 1));
        availability.setEndDate(LocalDate.of(2024, 12, 10));

        when(doctorAvailabilityRepository.findByDoctorId(Mockito.anyString()))
                .thenReturn(List.of(availability));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> appointmentServiceImpl.scheduleAppointment(appointment));
        verify(doctorAvailabilityRepository).findByDoctorId(eq("42"));
    }

    /**
     * Test {@link AppointmentServiceImpl#updateAppointment(Long, Appointment)}.
     * <ul>
     *   <li>Given an Appointment exists, update appointment details successfully.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#updateAppointment(Long, Appointment)}
     */
    @Test
    @DisplayName("Test updateAppointment(Long, Appointment); given existing Appointment, then update appointment")
    void testUpdateAppointment_givenExistingAppointment_thenUpdateAppointment() {
        // Arrange
        Doctor doctor = new Doctor();
        doctor.setDoctorId("42");
        doctor.setName("Dr. Jane Doe");

        Patient patient = new Patient();
        patient.setPatientId("42");
        patient.setName("John Doe");

        Appointment existingAppointment = new Appointment();
        existingAppointment.setAppointmentId(1L);
        existingAppointment.setDoctor(doctor);
        existingAppointment.setPatient(patient);
        existingAppointment.setStatus(Appointment.Status.Pending);

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setAppointmentDateTime(LocalDate.of(2024, 12, 6).atStartOfDay());
        updatedAppointment.setReason("Updated reason");
        updatedAppointment.setDoctor(doctor);
        updatedAppointment.setPatient(patient);

        Optional<Appointment> ofResult = Optional.of(existingAppointment);
        when(appointmentRepository.findById(Mockito.anyLong())).thenReturn(ofResult);
        when(appointmentRepository.save(Mockito.any(Appointment.class))).thenReturn(updatedAppointment);

        // Act
        Appointment result = appointmentServiceImpl.updateAppointment(1L, updatedAppointment);

        // Assert
        verify(appointmentRepository).findById(eq(1L));
        verify(appointmentRepository).save(isA(Appointment.class));
        assertSame(updatedAppointment, result);
    }

    /**
     * Test {@link AppointmentServiceImpl#updateAppointment(Long, Appointment)}.
     * <ul>
     *   <li>Then throw {@link IllegalArgumentException} if the Appointment does not exist.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppointmentServiceImpl#updateAppointment(Long, Appointment)}
     */
    @Test
    @DisplayName("Test updateAppointment(Long, Appointment); then throw IllegalArgumentException if Appointment not found")
    void testUpdateAppointment_thenThrowIllegalArgumentException() {
        // Arrange
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setAppointmentDateTime(LocalDate.of(2024, 12, 6).atStartOfDay());
        updatedAppointment.setReason("Updated reason");

        when(appointmentRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> appointmentServiceImpl.updateAppointment(1L, updatedAppointment));
        verify(appointmentRepository).findById(eq(1L));
    }

}
