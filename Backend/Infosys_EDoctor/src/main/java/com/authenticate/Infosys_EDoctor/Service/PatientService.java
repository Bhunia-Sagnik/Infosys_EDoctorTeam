package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.DTO.AppointmentRequest;
import com.authenticate.Infosys_EDoctor.DTO.PatientStatsDTO;
import com.authenticate.Infosys_EDoctor.Entity.*;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    public Patient addPatient(Patient patient);
    public Patient updateProfile(String patientId, Patient updatedPatient);
    public void deletePatient(String patientId);
    public List<Doctor> findDoctors();
    public Appointment updateAppointment(Long appointmentId, AppointmentRequest updatedAppointment);
    public void cancelAppointment(Long appointmentId, String reason);
    public Appointment makeAppointment(Appointment appointment);
    public List<Doctor> findDoctorsBySpecialization(String specialization);
    public List<DoctorAvailability> getAvailableDates(String doctorId);

    public Patient getPatientById(String patientId);

    public Patient getPatientByUserId(Long userId);

    List<Appointment> viewAppointments(String patientId);

    Optional<Patient> getPatientByEmail(String email);

    List<Doctor> findDoctorsByName(String doctorName);

    Doctor getDoctorById(String doctorId);

    List<Appointment> viewConfirmedAppointments(String patientId);

    List<Patient> getAllPatients();

    List<PatientStatsDTO> getAllPatientStats();

    PatientStatsDTO getPatientStatsById(String patientId);
}
