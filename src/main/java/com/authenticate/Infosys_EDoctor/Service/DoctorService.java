package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.DTO.DoctorStatsDTO;
import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DoctorService {
    Doctor addDoctor(Doctor doctor);
    Doctor getDoctorById(String doctorId);
    Doctor updateDoctor(String doctorId, Doctor doctor);
    void deleteDoctor(String doctorId);
    List<DoctorAvailability> getAvailableSlots(String doctorId);
    List<Doctor> findDoctorsBySpecialization(String specialization);

    List<Doctor> findAllDoctors();

    List<Appointment> getAllAppointments(String doctorId);

    Appointment confirmAppointment(Long appointmentId);

    void cancelAppointment(Long appontmentId, String reason);

    Doctor getDoctorByEmail(String email);

    List<Doctor> getDoctorsByName(String doctorName);

    List<DoctorStatsDTO> getAllDoctorStats();

    DoctorStatsDTO getDoctorStatsById(String doctorId);
}

