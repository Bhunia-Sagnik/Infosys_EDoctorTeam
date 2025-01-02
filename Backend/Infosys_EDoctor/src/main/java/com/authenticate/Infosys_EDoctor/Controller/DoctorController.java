package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;
import com.authenticate.Infosys_EDoctor.Service.NotificationService;
import com.authenticate.Infosys_EDoctor.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/{username}/addProfile")
    public ResponseEntity<?> addDoctor(@RequestBody Doctor doctor, @PathVariable String username) {
        // Check if the doctor is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in to add a profile.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Doctor existingDoctor = doctorService.getDoctorByEmail(user.getEmail());
        if (existingDoctor != null) {
            return ResponseEntity.badRequest().body("You already have a profile, you can update it.");
        }

        // Create profile
        doctor.setPassword(user.getPassword());
        doctor.setEmail(user.getEmail());
        Doctor newDoctor = doctorService.addDoctor(doctor);

        notificationService.sendDoctorProfileCreatedNotification(newDoctor.getEmail(), newDoctor.getDoctorId());

        return ResponseEntity.ok(newDoctor);
    }

    @GetMapping("/{username}/viewProfile")
    public ResponseEntity<?> getDoctorById(@PathVariable String username) {
        // Check if the doctor is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in to add a profile.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Doctor existingDoctor = doctorService.getDoctorByEmail(user.getEmail());
        if (existingDoctor == null) {
            return ResponseEntity.badRequest().body("You don't have a profile. Make one first.");
        }

        Doctor doctor = doctorService.getDoctorById(existingDoctor.getDoctorId());
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{username}/updateProfile")
    public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor, @PathVariable String username) {
        // Check if the doctor is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in to update a profile.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Doctor existingDoctor = doctorService.getDoctorByEmail(user.getEmail());
        if (existingDoctor == null) {
            return ResponseEntity.badRequest().body("You don't have a profile. Add one first.");
        }

        // Update profile
        Doctor updatedDoctor = doctorService.updateDoctor(existingDoctor.getDoctorId(), doctor);
        return ResponseEntity.ok(updatedDoctor);
    }

    @DeleteMapping("/deleteProfile")
    public ResponseEntity<String> deleteDoctor(@RequestParam String doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok("Doctor " + doctorId + " deleted successfully.");
    }

    @GetMapping("/{username}/available-slots")
    public ResponseEntity<?> getAvailableSlots(@PathVariable String username) {
        // Check if the doctor is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in to add a profile.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Doctor existingDoctor = doctorService.getDoctorByEmail(user.getEmail());
        if (existingDoctor == null) {
            return ResponseEntity.badRequest().body("You already have a profile, you can update it.");
        }

        List<DoctorAvailability> availableSlots = doctorService.getAvailableSlots(existingDoctor.getDoctorId());
        return ResponseEntity.ok(availableSlots);
    }

    @GetMapping("/specialized-doctors")
    public ResponseEntity<List<Doctor>> findDoctorBySpecialization(@RequestParam String specialization) {
        List<Doctor> doctors = doctorService.findDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{username}/viewAppointments")
    public ResponseEntity<?> getAllAppointments(@PathVariable String username) {
        // Check if the doctor is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in to add a profile.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Doctor existingDoctor = doctorService.getDoctorByEmail(user.getEmail());
        if (existingDoctor == null) {
            return ResponseEntity.badRequest().body("You already have a profile, you can update it.");
        }

        List<Appointment> appointments = doctorService.getAllAppointments(existingDoctor.getDoctorId());
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/confirm-appointment/{appointmentId}")
    public ResponseEntity<Appointment> confirmAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = doctorService.confirmAppointment(appointmentId);

        notificationService.sendAppointmentConfirmationToPatient(appointment);

        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/cancel-appointment/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId, @RequestParam String reason) {
        doctorService.cancelAppointment(appointmentId, reason);

        notificationService.sendAppointmentCancellationNotification(appointmentId, reason, true);

        return ResponseEntity.ok("Appointment cancelled successfully");
    }
}

