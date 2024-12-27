package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.Entity.*;
import com.authenticate.Infosys_EDoctor.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/{username}/feedback")
public class FeedbackController {

    @Autowired
    UserService userService;

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/submit/{doctorId}")
    public ResponseEntity<?> submitFeedback(@PathVariable String username, @RequestBody Feedback feedback, @PathVariable String doctorId) {
        // Check if the patient is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Optional<Patient> existingPatient = patientService.getPatientByEmail(user.getEmail());
        if (existingPatient.isEmpty()) {
            return ResponseEntity.badRequest().body("You don't have a profile. Make one first.");
        }

        Doctor doctor = doctorService.getDoctorById(doctorId);

        feedback.setPatient(existingPatient.get());
        feedback.setDoctor(doctor);

        Feedback savedFeedback = feedbackService.submitFeedback(feedback);
        return ResponseEntity.ok(savedFeedback);
    }

    // Get Feedback for a Doctor
    @GetMapping("/doctor")
    public ResponseEntity<?> getFeedbackForDoctor(@PathVariable String username) {
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

        List<Feedback> feedbackList = feedbackService.getFeedbackByDoctor(existingDoctor.getDoctorId());
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/doctorAvg")
    public ResponseEntity<?> getAvgFeedbackForDoctor(@PathVariable String username) {
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

        Double avgFeedback = feedbackService.getAvgFeedbackByDoctor(existingDoctor.getDoctorId());
        return ResponseEntity.ok(avgFeedback == null? "You have no feedbacks currently": avgFeedback);
    }

    // Get Feedback for a Patient
    @GetMapping("/patient")
    public ResponseEntity<?> getFeedbackForPatient(@PathVariable String username) {
        // Check if the patient is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Optional<Patient> existingPatient = patientService.getPatientByEmail(user.getEmail());
        if (existingPatient.isEmpty()) {
            return ResponseEntity.badRequest().body("You don't have a profile. Make one first.");
        }

        List<Feedback> feedbackList = feedbackService.getFeedbackByPatient(existingPatient.get().getPatientId());
        return ResponseEntity.ok(feedbackList);
    }

    // Get Doctors Pending Feedback by Patient
    @GetMapping("/pending-feedback")
    public ResponseEntity<?> getDoctorsPendingFeedback(@PathVariable String username) {
        // Check if the patient is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in.");
        }

        // Check if profile already exists
        User user = userService.getUserByUsername(username);
        Optional<Patient> existingPatient = patientService.getPatientByEmail(user.getEmail());
        if (existingPatient.isEmpty()) {
            return ResponseEntity.badRequest().body("You don't have a profile. Make one first.");
        }

        Patient patient = existingPatient.get();

        // Fetch all appointments for the patient
        List<Appointment> appointments = appointmentService.getAppointmentsForPatient(patient.getPatientId());

        // Get all doctors the patient has already provided feedback for
        List<String> doctorIdsWithFeedback = feedbackService.getDoctorsWithFeedbackByPatient(patient.getPatientId());

        // Filter doctors from appointments who don't have feedback
        List<Doctor> doctorsPendingFeedback = appointments.stream()
                .map(Appointment::getDoctor) // Extract doctors
                .filter(doctor -> !doctorIdsWithFeedback.contains(doctor.getDoctorId())) // Exclude already provided feedback
                .distinct() // Avoid duplicates
                .collect(Collectors.toList());

        return ResponseEntity.ok(doctorsPendingFeedback);
    }
}

