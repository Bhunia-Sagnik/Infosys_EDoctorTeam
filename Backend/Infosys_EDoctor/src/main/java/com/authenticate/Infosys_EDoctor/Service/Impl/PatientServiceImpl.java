package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.DTO.AppointmentRequest;
import com.authenticate.Infosys_EDoctor.DTO.PatientStatsDTO;
import com.authenticate.Infosys_EDoctor.Entity.*;
import com.authenticate.Infosys_EDoctor.Repository.PatientRepository;
import com.authenticate.Infosys_EDoctor.Service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    UserService userService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    AppointmentService appointmentService;

    // 1. Add Profile
    public Patient addPatient(Patient patient) {
        Optional<Patient> exists = patientRepository.findByEmail(patient.getEmail());
        if(exists.isPresent()) {
            throw new RuntimeException("Patient with given email already exists");
        }

        String id = "PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        patient.setPatientId(id);
        Patient savedPatient = patientRepository.save(patient);

        return savedPatient;
    }

    // 2. Update Profile
    @Override
    public Patient updateProfile(String patientId, Patient updatedPatient) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));

        // Update only non-null and non-empty fields
        if (updatedPatient.getName() != null && !updatedPatient.getName().isEmpty()) {
            existingPatient.setName(updatedPatient.getName());
        }
        if (updatedPatient.getMobileNo() != null && !updatedPatient.getMobileNo().isEmpty()) {
            existingPatient.setMobileNo(updatedPatient.getMobileNo());
        }
        if (updatedPatient.getBloodGroup() != null && !updatedPatient.getBloodGroup().isEmpty()) {
            existingPatient.setBloodGroup(updatedPatient.getBloodGroup());
        }
        if (updatedPatient.getGender() != null) {
            existingPatient.setGender(updatedPatient.getGender());
        }
        if (updatedPatient.getAge() > 0) {
            existingPatient.setAge(updatedPatient.getAge());
        }
        if (updatedPatient.getAddress() != null && !updatedPatient.getAddress().isEmpty()) {
            existingPatient.setAddress(updatedPatient.getAddress());
        }

        return patientRepository.save(existingPatient);
    }

    @Override
    public void deletePatient(String patientId) {
        Patient existingPatient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + patientId));

        patientRepository.delete(existingPatient);
    }

    // 3. Find Doctors
    public List<Doctor> findDoctors() {

        return doctorService.findAllDoctors(); // Fetches all doctors
    }

    // 4. Make Appointment
    public Appointment makeAppointment(Appointment appointment) {
        return appointmentService.scheduleAppointment(appointment);
    }

    // 5. Update Appointment
    public Appointment updateAppointment(Long appointmentId, AppointmentRequest updatedAppointment) {
        Appointment existingAppointment = appointmentService.getAppointmentById(appointmentId);

        Appointment appointment = new Appointment();

        appointment.setAppointmentId(existingAppointment.getAppointmentId());
        appointment.setPatient(existingAppointment.getPatient());
        appointment.setDoctor(existingAppointment.getDoctor());
        appointment.setStatus(existingAppointment.getStatus());
        appointment.setReason(updatedAppointment.getReason());
        appointment.setAppointmentDateTime(LocalDateTime.parse(updatedAppointment.getAppointmentDateTime()));

        return appointmentService.updateAppointment(appointmentId, appointment);
    }

    // 6. Cancel Appointment
    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId, String reason) {
        appointmentService.cancelAppointment(appointmentId, reason);
    }

    public List<Doctor> findDoctorsBySpecialization(String specialization) {
        return doctorService.findDoctorsBySpecialization(specialization);
    }

    // Check Available Dates for a Particular Doctor
    public List<DoctorAvailability> getAvailableDates(String doctorId) {
        return doctorAvailabilityService.getAvailabilityByDoctorId(doctorId); // Assuming Doctor entity has a list of available dates
    }

    @Override
    public Patient getPatientById(String patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient with " + patientId + " not found"));

        return patient;
    }

    @Override
    public Patient getPatientByUserId(Long userId) {
        Optional<Patient> patient = patientRepository.findByEmail(userService.getUserById(userId).getEmail());
        if(patient.isEmpty()) {
            throw new RuntimeException("Patient doest not exist");
        }

        return patient.get();
    }

    @Override
    public List<Appointment> viewAppointments(String patientId) {
        return appointmentService.getAppointmentsForPatient(patientId);
    }

    @Override
    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    @Override
    public List<Doctor> findDoctorsByName(String doctorName) {
        return doctorService.getDoctorsByName(doctorName);
    }

    @Override
    public Doctor getDoctorById(String doctorId) {
        return doctorService.getDoctorById(doctorId);
    }

    @Override
    public List<Appointment> viewConfirmedAppointments(String patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patientId);
        List<Appointment> confirmedAppointments = new ArrayList<>();

        for(Appointment appointment: appointments) {
            if(appointment.getStatus() == Appointment.Status.Confirmed) {
                confirmedAppointments.add(appointment);
            }
        }

        return confirmedAppointments;
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public List<PatientStatsDTO> getAllPatientStats() {
        // Fetch all doctors from the repository
        List<Patient> patients = patientRepository.findAll();

        // Process each doctor to calculate their stats
        return patients.stream().map(patient -> {
            // Fetch all appointments for the current doctor
            List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patient.getPatientId());

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
            return new PatientStatsDTO(
                    patient.getPatientId(),
                    patient.getName(),
                    patient.getEmail(),
                    patient.getMobileNo(),
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
    public PatientStatsDTO getPatientStatsById(String patientId) {
        Patient patient = getPatientById(patientId);

        // Fetch all appointments for the doctor
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patientId);

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

        return new PatientStatsDTO(
                patient.getPatientId(),
                patient.getName(),
                patient.getEmail(),
                patient.getMobileNo(),
                totalAppointments,
                pendingAppointments,
                cancelledAppointments,
                confirmedAppointments,
                paidAppointments,
                unpaidAppointments
        );
    }
}
