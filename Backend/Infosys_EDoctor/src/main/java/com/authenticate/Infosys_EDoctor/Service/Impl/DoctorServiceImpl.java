package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.DTO.DoctorStatsDTO;
import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Repository.DoctorRepository;
import com.authenticate.Infosys_EDoctor.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    UserService userService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    @Autowired
    private AppointmentService appointmentService;

    @Override
    public Doctor addDoctor(Doctor doctor) {
        Doctor exists = doctorRepository.findByEmail(doctor.getEmail());
        if(exists != null) {
            throw new RuntimeException("Doctor profile already exists");
        }

        String id = "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        doctor.setDoctorId(id);
        Doctor savedDoc = doctorRepository.save(doctor);

        return savedDoc;
    }

    @Override
    public Doctor getDoctorById(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return doctor;
    }

    @Override
    public Doctor updateDoctor(String doctorId, Doctor updatedDoctor) {
        // Fetch the existing doctor from the repository
        Doctor existingDoctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found."));

        // Update only the fields provided in the updatedDoctor object
        if (updatedDoctor.getName() != null && !updatedDoctor.getName().isEmpty()) {
            existingDoctor.setName(updatedDoctor.getName());
        }
        if (updatedDoctor.getSpecialization() != null && !updatedDoctor.getSpecialization().isEmpty()) {
            existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        }
        if (updatedDoctor.getLocation() != null && !updatedDoctor.getLocation().isEmpty()) {
            existingDoctor.setLocation(updatedDoctor.getLocation());
        }
        if (updatedDoctor.getHospitalName() != null && !updatedDoctor.getHospitalName().isEmpty()) {
            existingDoctor.setHospitalName(updatedDoctor.getHospitalName());
        }
        if (updatedDoctor.getMobileNo() != null && !updatedDoctor.getMobileNo().isEmpty()) {
            existingDoctor.setMobileNo(updatedDoctor.getMobileNo());
        }
        if (updatedDoctor.getEmail() != null && !updatedDoctor.getEmail().isEmpty()) {
            existingDoctor.setEmail(updatedDoctor.getEmail());
        }

        // Save and return the updated doctor
        return doctorRepository.save(existingDoctor);
    }

    @Override
    public void deleteDoctor(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found."));
        doctorRepository.delete(doctor);
    }

    @Override
    public List<DoctorAvailability> getAvailableSlots(String doctorId) {
        Doctor doctor = doctorRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found."));
        return doctorAvailabilityService.getAvailabilityByDoctorId(doctorId);
    }

    @Override
    public List<Doctor> findDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Doctor> fetched = new ArrayList<>();

        for(Doctor doctor: doctors) {
            if(doctor.getSpecialization().equalsIgnoreCase(specialization)) {
                fetched.add(doctor);
            }
        }
        return fetched;
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Appointment> getAllAppointments(String doctorId) {
        return appointmentService.getAppointmentsForDoctor(doctorId);
    }

    @Override
    public Appointment confirmAppointment(Long appointmentId) {
        return appointmentService.confirmAppointment(appointmentId);
    }

    @Override
    public void cancelAppointment(Long appointmentId, String reason) {
        appointmentService.cancelAppointment(appointmentId, reason);
    }

    @Override
    public Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public List<Doctor> getDoctorsByName(String doctorName) {
        if (doctorName == null || doctorName.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Fetch all doctors
        List<Doctor> doctors = doctorRepository.findAll();
        List<Doctor> fetched = new ArrayList<>();

        // Loop through all doctors and check for case-insensitive matching
        for (Doctor doctor : doctors) {
            if (doctor.getName().toLowerCase().contains(doctorName.trim().toLowerCase())) {
                fetched.add(doctor);
            }
        }

        return fetched;
    }

    @Override
    public List<DoctorStatsDTO> getAllDoctorStats() {
        // Fetch all doctors from the repository
        List<Doctor> doctors = doctorRepository.findAll();

        // Process each doctor to calculate their stats
        return doctors.stream().map(doctor -> {
            // Fetch all appointments for the current doctor
            List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctor.getDoctorId());

            long totalAppointments = appointments.size();
            long pendingAppointments = appointments.stream()
                    .filter(appointment -> appointment.getStatus() == Appointment.Status.Pending)
                    .count();
            long cancelledAppointments = appointments.stream()
                    .filter(appointment -> appointment.getStatus() == Appointment.Status.Cancelled)
                    .count();
            long confirmedAppointments = appointments.stream()
                    .filter(appointment -> appointment.getStatus() == Appointment.Status.Confirmed)
                    .count();
            long paidAppointments = appointments.stream()
                    .filter(appointment -> appointment.getStatus() == Appointment.Status.Confirmed && appointment.isPaid())
                    .count();
            long unpaidAppointments = appointments.stream()
                    .filter(appointment -> appointment.getStatus() == Appointment.Status.Confirmed && !appointment.isPaid())
                    .count();

            // Create and return the DoctorDTO for this doctor
            return new DoctorStatsDTO(
                    doctor.getDoctorId(),
                    doctor.getName(),
                    doctor.getEmail(),
                    doctor.getMobileNo(),
                    doctor.getSpecialization(),
                    totalAppointments,
                    pendingAppointments,
                    cancelledAppointments,
                    confirmedAppointments,
                    paidAppointments,
                    unpaidAppointments
            );
        }).collect(Collectors.toList());
    }

    @Override
    public DoctorStatsDTO getDoctorStatsById(String doctorId) {
        Doctor doctor = getDoctorById(doctorId);

        // Fetch all appointments for the doctor
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctor(doctorId);

        long totalAppointments = appointments.size();
        long pendingAppointments = appointments.stream()
                .filter(appointment -> appointment.getStatus() == Appointment.Status.Pending)
                .count();
        long cancelledAppointments = appointments.stream()
                .filter(appointment -> appointment.getStatus() == Appointment.Status.Cancelled)
                .count();
        long confirmedAppointments = appointments.stream()
                .filter(appointment -> appointment.getStatus() == Appointment.Status.Confirmed)
                .count();
        long paidAppointments = appointments.stream()
                .filter(appointment -> appointment.getStatus() == Appointment.Status.Confirmed && appointment.isPaid())
                .count();
        long unpaidAppointments = appointments.stream()
                .filter(appointment -> appointment.getStatus() == Appointment.Status.Confirmed && !appointment.isPaid())
                .count();

        return new DoctorStatsDTO(
                doctor.getDoctorId(),
                doctor.getName(),
                doctor.getEmail(),
                doctor.getMobileNo(),
                doctor.getSpecialization(),
                totalAppointments,
                pendingAppointments,
                cancelledAppointments,
                confirmedAppointments,
                paidAppointments,
                unpaidAppointments
        );
    }
}

