package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.DTO.AppointmentRequest;
import com.authenticate.Infosys_EDoctor.Entity.*;
import com.authenticate.Infosys_EDoctor.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/{username}/patient")
public class PatientController {

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    NotificationService notificationService;

    // 1. Add Profile
    @PostMapping("/addProfile")
    public ResponseEntity<?> addProfile(@RequestBody Patient patient, @PathVariable String username) {
        // Check if the doctor is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Optional<Patient> existingPatient = patientService.getPatientByEmail(user.getEmail());
        if (existingPatient.isPresent()) {
            return ResponseEntity.badRequest().body("You already have a profile, you can update it.");
        }

        // Create profile
        patient.setPassword(user.getPassword());
        patient.setEmail(user.getEmail());
        Patient savedPatient = patientService.addPatient(patient);

        notificationService.sendPatientProfileCreatedNotification(savedPatient.getEmail(), savedPatient.getPatientId());

        return ResponseEntity.ok(savedPatient);
    }

    // 2. Update Profile
    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody Patient patient, @PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(403).body("User not found or not logged in.");
        }

        Optional<Patient> existingPatientOpt = patientService.getPatientByEmail(user.getEmail());
        if (existingPatientOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("No existing profile found. Please create a profile first.");
        }

        Patient existingPatient = existingPatientOpt.get();
        Patient updatedPatient = patientService.updateProfile(existingPatient.getPatientId(), patient);

        return ResponseEntity.ok(updatedPatient);
    }

    // 3. View Patient Details
    @GetMapping("/viewProfile")
    public ResponseEntity<?> viewPatientDetails(@PathVariable String username) {
        // Check if the doctor is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Optional<Patient> existingPatient = patientService.getPatientByEmail(user.getEmail());
        if (existingPatient.isEmpty()) {
            return ResponseEntity.badRequest().body("You already have a profile, you can update it.");
        }

        Patient oldPatient = existingPatient.get();

        return ResponseEntity.ok(patientService.getPatientById(oldPatient.getPatientId()));
    }

    // 3. Find Doctors
    @GetMapping("/findDoctors")
    public ResponseEntity<List<Doctor>> findDoctors() {
        List<Doctor> doctors = patientService.findDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/findDoctorsByName")
    public ResponseEntity<List<Doctor>> findDoctorsByName(@RequestParam String doctorName) {
        List<Doctor> doctors = patientService.findDoctorsByName(doctorName);
        return ResponseEntity.ok(doctors);
    }

    // Find Doctors by Specialization
    @GetMapping("/findDoctorsBySpecialization")
    public ResponseEntity<List<Doctor>> findDoctorsBySpecialization(@RequestParam String specialization) {
        return ResponseEntity.ok(patientService.findDoctorsBySpecialization(specialization));
    }

    @GetMapping("/getDoctorById")
    public ResponseEntity<Doctor> getDoctorById(@RequestParam String doctorId) {
        Doctor doctor = patientService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctor);
    }

    // Check Available Dates for a Particular Doctor
    @GetMapping("/doctorAvailableDates")
    public ResponseEntity<List<DoctorAvailability>> getAvailableDates(@RequestParam String doctorId) {
        return ResponseEntity.ok(patientService.getAvailableDates(doctorId));
    }

    // 4. Make Appointment
    @PostMapping("/makeAppointment")
    public ResponseEntity<?> makeAppointment(@RequestBody AppointmentRequest request, @PathVariable String username) {
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in.");
        }

        User user = userService.getUserByUsername(username);
        Optional<Patient> patientOpt = patientService.getPatientByEmail(user.getEmail());
        if (patientOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("You don't have a profile yet. Create one first.");
        }

        // Fetch related doctor
        Doctor doctor = patientService.getDoctorById(request.getDoctorId());

        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patientOpt.get());
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(LocalDateTime.parse(request.getAppointmentDateTime()));
        appointment.setReason(request.getReason());
        appointment.setStatus(Appointment.Status.Pending);

        Appointment savedAppointment = patientService.makeAppointment(appointment);

        notificationService.sendNewAppointmentNotificationToPatient(savedAppointment);
        notificationService.sendNewAppointmentNotificationToDoctor(savedAppointment);
        return ResponseEntity.ok(savedAppointment);
    }



    @GetMapping("/viewAppointments")
    public ResponseEntity<?> viewAppointments(@PathVariable String username) {
        // Check if the doctor is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Optional<Patient> existingPatient = patientService.getPatientByEmail(user.getEmail());
        if (existingPatient.isEmpty()) {
            return ResponseEntity.badRequest().body("You already have a profile, you can update it.");
        }

        Patient oldPatient = existingPatient.get();

        return ResponseEntity.ok(patientService.viewAppointments(oldPatient.getPatientId()));
    }

    @GetMapping("/viewConfirmedAppointments")
    public ResponseEntity<?> viewConfirmedAppointments(@PathVariable String username) {
        // Check if the doctor is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Optional<Patient> existingPatient = patientService.getPatientByEmail(user.getEmail());
        if (existingPatient.isEmpty()) {
            return ResponseEntity.badRequest().body("You don't have a profile. Make one first.");
        }

        Patient oldPatient = existingPatient.get();

        return ResponseEntity.ok(patientService.viewConfirmedAppointments(oldPatient.getPatientId()));
    }

    // 5. Update Appointment
    @PutMapping("/updateAppointment/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentRequest appointmentRequest) {
        Appointment updatedAppointment = patientService.updateAppointment(appointmentId, appointmentRequest);

        notificationService.sendUpdatedAppointmentNotificationToPatient(updatedAppointment);

        return ResponseEntity.ok(updatedAppointment);
    }

    // 6. Cancel Appointment
    @PutMapping("/cancelAppointment/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId, @RequestParam String reason) {
        patientService.cancelAppointment(appointmentId, reason);

        notificationService.sendAppointmentCancellationNotification(appointmentId, reason, false);

        return ResponseEntity.ok("Appointment canceled successfully");
    }
}

