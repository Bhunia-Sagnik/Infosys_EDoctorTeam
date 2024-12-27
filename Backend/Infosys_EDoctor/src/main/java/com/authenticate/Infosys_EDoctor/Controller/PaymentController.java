package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Service.Impl.PaymentService;
import com.authenticate.Infosys_EDoctor.Service.PatientService;
import com.authenticate.Infosys_EDoctor.Service.UserService;
import com.razorpay.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/{username}/payments")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate/{appointmentId}")
    public ResponseEntity<?> initiatePayment(@PathVariable Long appointmentId, @PathVariable String username) {
        // Log the appointmentId and username for debugging
        System.out.println("Initiating payment for Appointment ID: " + appointmentId + " with Username: " + username);

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

        try {
            // Initiate the payment and verify it right after
            Map<String, Object> orderDetails = paymentService.initiatePayment(appointmentId);
            return ResponseEntity.ok(orderDetails);  // This will return the order details or verification result
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/verify/{appointmentId}")
    public ResponseEntity<?> verifyPayment(@PathVariable Long appointmentId, @RequestParam String paymentId, @PathVariable String username) {
        // Check if the user is logged in
        if (username == null) {
            return ResponseEntity.status(403).body("You must be logged in.");
        }

        try {
            // Call the service to verify the payment
            String verificationResult = paymentService.verifyPayment(appointmentId, paymentId);

            return ResponseEntity.ok(verificationResult);  // Return the verification result (success or failure)
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error verifying payment: " + e.getMessage());
        }
    }

    @GetMapping("/generateInvoice/{appointmentId}")
    public ResponseEntity<?> generateInvoice(@PathVariable Long appointmentId) {
        ByteArrayInputStream pdf = paymentService.getInvoice(appointmentId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline;file=invoice+appt" + appointmentId + ".pdf");

        return ResponseEntity.ok().
                headers(httpHeaders).
                contentType(MediaType.APPLICATION_PDF).
                body(new InputStreamResource(pdf));
    }
}


