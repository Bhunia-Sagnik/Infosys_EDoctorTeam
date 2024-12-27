package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.DTO.*;
import com.authenticate.Infosys_EDoctor.Entity.Admin;
import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {
    Admin addAdmin(Admin admin);

    String verifyAdmin(String adminId);

    Admin updateAdmin(String adminId, Admin admin);

    void deleteAdmin(String adminId);

    List<Patient> getAllPatients();

    Patient updatePatient(String patientId, Patient patient);

    void deletePatient(String patientId);

    List<Doctor> getAllDoctors();

    Doctor updateDoctor(String doctorId, Doctor doctor);

    void deleteDoctor(String doctorId);

    List<Appointment> getAllAppointments();

    List<Appointment> getAppointmentByPatientId(String patientId);

    List<Appointment> getAppointmentByDoctorId(String doctorId);

    void deleteAppointment(Long id);

    Appointment updateAppointment(Long id, AppointmentRequest appointmentRequest);

    Appointment addAppointment(AppointmentRequest appointmentRequest);

    PatientStatsDTO getPatientStatsById(String patientId);

    List<PatientStatsDTO> getAllPatientStats();

    List<DoctorStatsDTO> getAllDoctorStats();

    DoctorStatsDTO getDoctorStatsById(String doctorId);

    WebStatsBetweenDTO getWebStatsBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<WebStatsDTO> getWebStats();

    Admin getAdminById(String adminId);

    Patient addPatient(String patientUsername, Patient patient);

    Doctor addDoctor(String doctorUsername, Doctor doctor);

    Object getProfile(@NotBlank(message = "Email is mandatory") @Email(message = "Invalid email address") String email);
}
