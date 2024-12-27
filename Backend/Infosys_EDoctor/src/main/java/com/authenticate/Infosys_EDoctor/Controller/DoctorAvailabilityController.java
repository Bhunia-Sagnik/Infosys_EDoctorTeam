package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Entity.User;
import com.authenticate.Infosys_EDoctor.Service.DoctorAvailabilityService;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;
import com.authenticate.Infosys_EDoctor.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor/{username}/availability")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityService availabilityService;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/addAvailability")
    public ResponseEntity<?> addAvailability(@RequestBody DoctorAvailability availability, @PathVariable String username) {
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

        availability.setDoctorId(existingDoctor.getDoctorId());

        DoctorAvailability savedAvailability = availabilityService.addAvailability(availability);
        return ResponseEntity.ok(savedAvailability);
    }

    @GetMapping("/viewAvailability")
    public ResponseEntity<?> getAvailability(@PathVariable String username) {
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

        List<DoctorAvailability> availabilities = availabilityService.getAvailabilityByDoctorId(existingDoctor.getDoctorId());
        return ResponseEntity.ok(availabilities);
    }

    @PutMapping("/updateAvailability")
    public ResponseEntity<?> updateAvailability(@RequestParam int id, @RequestBody DoctorAvailability availability) {
        DoctorAvailability updatedAvailability = availabilityService.updateAvailability(id, availability);
        return ResponseEntity.ok(updatedAvailability);
    }

    @DeleteMapping("/deleteAvailability")
    public ResponseEntity<?> deleteAvailability(@RequestParam int id) {
        boolean deleted = availabilityService.deleteAvailability(id);
        return deleted? ResponseEntity.ok("Availability deleted successfully"): ResponseEntity.badRequest().body("Enter valid credentials");
    }
}

