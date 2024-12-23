package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.DTO.DoctorStatsDTO;
import com.authenticate.Infosys_EDoctor.DTO.PatientStatsDTO;
import com.authenticate.Infosys_EDoctor.DTO.WebStatsBetweenDTO;
import com.authenticate.Infosys_EDoctor.DTO.WebStatsDTO;
import com.authenticate.Infosys_EDoctor.Entity.*;
import com.authenticate.Infosys_EDoctor.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/{username}/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    UserService userService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    NotificationService notificationService;

    private boolean checkLoginAndValid(String username) {
        if(username == null) {
            return false;
        }

        User user = userService.getUserByUsername(username);
        return user != null;
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<?> addAdmin(@RequestBody Admin admin, @PathVariable String username) {
        if(username == null) {
            ResponseEntity.badRequest().body("Enter username to add admin.");
        }

        User user = userService.getUserByUsername(username);
        if(user == null) {
            ResponseEntity.badRequest().body("Enter valid username to add admin.");
        }

        admin.setEmail(user.getEmail());
        admin.setPassword(user.getPassword());

        Admin savedAdmin = adminService.addAdmin(admin);

        notificationService.sendAdminProfileCreatedNotification(savedAdmin.getEmail(), savedAdmin.getAdminId());

        return ResponseEntity.ok(savedAdmin);
    }

    @PutMapping("/updateAdmin/{adminId}")
    public ResponseEntity<?> updateAdmin(@PathVariable String adminId, @RequestBody Admin admin) {
        Admin updatedAdmin = adminService.updateAdmin(adminId, admin);

        notificationService.sendAdminProfileUpdatedNotification(updatedAdmin.getEmail(), updatedAdmin.getAdminId());

        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/deleteAdmin/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable String adminId) {
        Admin admin = adminService.getAdminById(adminId);

        adminService.deleteAdmin(adminId);

        notificationService.sendAdminProfileDeletedNotification(admin.getEmail(), adminId);

        return ResponseEntity.ok("Admin with adminId " + adminId + " deleted successfully.");
    }

    @GetMapping("/verifyAdmin/{adminId}")
    public ResponseEntity<?> verifyAdmin(@PathVariable String adminId, @PathVariable String username) {
        if(checkLoginAndValid(username)) {
            String name = adminService.verifyAdmin(adminId);

            if(name != null) {
                return ResponseEntity.ok("Welcome " + name);
            }

            return ResponseEntity.badRequest().body("Your adminId is invalid. Enter valid id to access dashboard");
        }

        return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
    }

    // --- Patient Management ---
    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients(@PathVariable String username) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        List<Patient> patients = adminService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/patientUpdate/{patientId}")
    public ResponseEntity<?> updatePatient(@PathVariable String username, @PathVariable String patientId, @RequestBody Patient patient) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        Patient updatedPatient = adminService.updatePatient(patientId, patient);

        notificationService.sendProfileUpdatedByAdminNotification(updatedPatient.getEmail(), updatedPatient.getPatientId());

        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/patientDelete/{patientId}")
    public ResponseEntity<String> deletePatient(@PathVariable String username, @PathVariable String patientId) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        Patient patient = patientService.getPatientById(patientId);

        adminService.deletePatient(patientId);

        notificationService.sendProfileDeletedByAdminNotification(patient.getEmail(), patientId);

        return ResponseEntity.ok("Patient with patientId '" + patientId + "' deleted successfully.");
    }

    // --- Doctor Management ---
    @GetMapping("/doctors")
    public ResponseEntity<?> getAllDoctors(@PathVariable String username) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        List<Doctor> doctors = adminService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @PutMapping("/doctorUpdate/{doctorId}")
    public ResponseEntity<?> updateDoctor(@PathVariable String username, @PathVariable String doctorId, @RequestBody Doctor doctor) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        Doctor updatedDoctor = adminService.updateDoctor(doctorId, doctor);

        notificationService.sendProfileUpdatedByAdminNotification(updatedDoctor.getEmail(), updatedDoctor.getDoctorId());

        return ResponseEntity.ok(doctor);
    }

    @DeleteMapping("/doctorDelete/{doctorId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable String username, @PathVariable String doctorId) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        Doctor doctor = doctorService.getDoctorById(doctorId);

        adminService.deleteDoctor(doctorId);

        notificationService.sendProfileDeletedByAdminNotification(doctor.getEmail(), doctorId);

        return ResponseEntity.ok("Patient with doctorId '" + doctorId + "' deleted successfully.");
    }

    // --- Appointment Management ---
    @PostMapping("/appointmentAdd")
    public ResponseEntity<?> addAppointment(@PathVariable String username, @RequestBody Appointment appointment) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        Appointment createdAppointment = adminService.addAppointment(appointment);

        notificationService.sendNewAppointmentNotificationToDoctor(createdAppointment);
        notificationService.sendNewAppointmentNotificationToPatient(createdAppointment);

        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/appointmentUpdate/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable String username, @PathVariable Long id, @RequestBody Appointment appointment) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        Appointment updatedAppointment = adminService.updateAppointment(id, appointment);

        notificationService.sendUpdatedAppointmentNotificationToPatient(updatedAppointment);

        return ResponseEntity.ok(updatedAppointment);
    }

    @DeleteMapping("/appointmentDelete/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable String username, @PathVariable Long id) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        adminService.deleteAppointment(id);

        notificationService.sendAppointmentCancellationNotification(id, "Cancelled by Admin", true);
        notificationService.sendAppointmentCancellationNotification(id, "Cancelled by Admin", false);

        return ResponseEntity.ok("Appointment with ID " + id + " deleted successfully.");
    }

    @GetMapping("/appointmentByPatientId")
    public ResponseEntity<?> getAppointmentByPatientId(@PathVariable String username, @RequestParam String patientId) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        List<Appointment> appointments = adminService.getAppointmentByPatientId(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointmentByDoctorId")
    public ResponseEntity<?> getAppointmentByDoctorId(@PathVariable String username, @RequestParam String doctorId) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        List<Appointment> appointments = adminService.getAppointmentByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> getAllAppointments(@PathVariable String username) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        List<Appointment> appointments = adminService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    // --- Website Stats ---
    @GetMapping("/allPatientStats")
    public ResponseEntity<?> getPatientStats(@PathVariable String username) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        List<PatientStatsDTO> patientStats = adminService.getAllPatientStats();

        return ResponseEntity.ok(patientStats);
    }

    @GetMapping("/allDoctorStats")
    public ResponseEntity<?> getDoctorStats(@PathVariable String username) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        List<DoctorStatsDTO> doctorStats = adminService.getAllDoctorStats();

        return ResponseEntity.ok(doctorStats);
    }

    @GetMapping("/patientStats/{patientId}")
    public ResponseEntity<?> getPatientStatsById(@PathVariable String username, @PathVariable String patientId) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        PatientStatsDTO patientStatsDTO = adminService.getPatientStatsById(patientId);

        return ResponseEntity.ok(patientStatsDTO);
    }
//
    @GetMapping("/doctorStats/{doctorId}")
    public ResponseEntity<?> getDoctorStatsById(@PathVariable String username, @PathVariable String doctorId) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        DoctorStatsDTO doctorStatsDTO = adminService.getDoctorStatsById(doctorId);

        return ResponseEntity.ok(doctorStatsDTO);
    }

    @GetMapping("/webStatsBetween")
    public ResponseEntity<?> getWebStatsBetween(@PathVariable String username, @RequestParam String startDate, @RequestParam String endDate) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        WebStatsBetweenDTO webStatsDTO = adminService.getWebStatsBetween(LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));

        return ResponseEntity.ok(webStatsDTO);
    }

    @GetMapping("/webStats")
    public ResponseEntity<?> getWebStats(@PathVariable String username) {
        if(!checkLoginAndValid(username)) {
            return ResponseEntity.badRequest().body("Create an admin profile to access. \nAlready have a profile? Login.");
        }

        List<WebStatsDTO> webStats = adminService.getWebStats();

        return ResponseEntity.ok(webStats);
    }
}
