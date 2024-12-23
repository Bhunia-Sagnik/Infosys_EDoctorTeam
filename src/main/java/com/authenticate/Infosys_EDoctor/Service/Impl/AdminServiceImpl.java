package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.DTO.DoctorStatsDTO;
import com.authenticate.Infosys_EDoctor.DTO.PatientStatsDTO;
import com.authenticate.Infosys_EDoctor.DTO.WebStatsBetweenDTO;
import com.authenticate.Infosys_EDoctor.DTO.WebStatsDTO;
import com.authenticate.Infosys_EDoctor.Entity.Admin;
import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Repository.AdminRepository;
import com.authenticate.Infosys_EDoctor.Service.AdminService;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;
import com.authenticate.Infosys_EDoctor.Service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PatientService patientService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    AppointmentService appointmentService;

    // 1. Add Admin
    @Override
    public Admin addAdmin(Admin admin) {
        Optional<Admin> exists = adminRepository.findByEmail(admin.getEmail());
        if (exists.isPresent()) {
            throw new RuntimeException("Admin already exists");
        }

        String id = "ADM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        admin.setAdminId(id);

        return adminRepository.save(admin);
    }

    // 2. Update Admin Profile
    @Override
    @Transactional
    public Admin updateAdmin(String adminId, Admin updatedAdmin) {
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + adminId));

        existingAdmin.setName(updatedAdmin.getName() != null? updatedAdmin.getName() : existingAdmin.getName());
        existingAdmin.setMobileNo(updatedAdmin.getMobileNo() != null? updatedAdmin.getMobileNo(): existingAdmin.getMobileNo());

        return adminRepository.save(existingAdmin);
    }

    // 3. Verify Admin
    @Override
    public String verifyAdmin(String adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));
        return admin.getName();
    }

    // 4. Delete Admin
    @Override
    @Transactional
    public void deleteAdmin(String adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        adminRepository.delete(admin);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @Override
    public Patient updatePatient(String patientId, Patient patient) {
        return patientService.updateProfile(patientId, patient);
    }

    @Override
    public void deletePatient(String patientId) {
        patientService.deletePatient(patientId);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorService.findAllDoctors();
    }

    @Override
    public Doctor updateDoctor(String doctorId, Doctor doctor) {
        return doctorService.updateDoctor(doctorId, doctor);
    }

    @Override
    public void deleteDoctor(String doctorId) {
        doctorService.deleteDoctor(doctorId);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @Override
    public List<Appointment> getAppointmentByPatientId(String patientId) {
        return appointmentService.getAppointmentsForPatient(patientId);
    }

    @Override
    public List<Appointment> getAppointmentByDoctorId(String doctorId) {
        return appointmentService.getAppointmentsForDoctor(doctorId);
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentService.cancelAppointment(id, "Cancelled by Admin");
    }

    @Override
    public Appointment updateAppointment(Long id, Appointment appointment) {
        return appointmentService.updateAppointment(id, appointment);
    }

    @Override
    public Appointment addAppointment(Appointment appointment) {
        return appointmentService.scheduleAppointment(appointment);
    }

    @Override
    public PatientStatsDTO getPatientStatsById(String patientId) {
        return patientService.getPatientStatsById(patientId);
    }

    @Override
    public List<PatientStatsDTO> getAllPatientStats() {
        return patientService.getAllPatientStats();
    }

    @Override
    public List<DoctorStatsDTO> getAllDoctorStats() {
        return doctorService.getAllDoctorStats();
    }

    @Override
    public DoctorStatsDTO getDoctorStatsById(String doctorId) {
        return doctorService.getDoctorStatsById(doctorId);
    }

    @Override
    public WebStatsBetweenDTO getWebStatsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        // Fetch all appointments between the given dates
        List<Appointment> appointments = appointmentService.findAppointmentsBetweenDates(startDate, endDate);

        // Initialize counters
        int totalAppointments = appointments.size();
        int pendingAppointments = 0;
        int confirmedAppointments = 0;
        int cancelledAppointments = 0;
        int paidConfirmedAppointments = 0;
        int unpaidConfirmedAppointments = 0;

        // Calculate stats
        for (Appointment appointment : appointments) {
            switch (appointment.getStatus()) {
                case Pending -> pendingAppointments++;
                case Confirmed -> {
                    confirmedAppointments++;
                    if (appointment.isPaid()) {
                        paidConfirmedAppointments++;
                    } else {
                        unpaidConfirmedAppointments++;
                    }
                }
                case Cancelled -> cancelledAppointments++;
            }
        }

        // Create and return WebStatsDTO
        return new WebStatsBetweenDTO(
                startDate,
                endDate,
                totalAppointments,
                pendingAppointments,
                confirmedAppointments,
                cancelledAppointments,
                paidConfirmedAppointments,
                unpaidConfirmedAppointments
        );
    }

    @Override
    public List<WebStatsDTO> getWebStats() {
        List<Object[]> groupedAppointments = appointmentService.getAppointmentsGroupedByDate();

        // Use a map to accumulate statistics by date
        Map<String, WebStatsDTO> statsMap = new HashMap<>();

        // Process each record from the grouped results
        for (Object[] record : groupedAppointments) {
            // Assuming record[0] is a java.sql.Date or String representation of the date
            String date = (String) record[0];  // The date as String (yyyy-MM-dd)

            // Status as an enum (convert it to a String)
            Appointment.Status status = (Appointment.Status) record[1];
            String statusString = status.name();  // Converts Status enum to its string value (Pending, Confirmed, Cancelled)

            // Payment status (converted to boolean or other suitable type)
            Boolean paid = (Boolean) record[2]; // Payment status (Paid or Unpaid)

            long count = (long) record[3]; // Number of appointments for this combination

            // Get the existing WebStatsDTO or create a new one
            WebStatsDTO statsDTO = statsMap.getOrDefault(date, new WebStatsDTO(date, 0, 0, 0, 0, 0, 0));

            // Update statistics based on the status
            statsDTO.setTotalAppointments(statsDTO.getTotalAppointments() + count);

            if ("PENDING".equalsIgnoreCase(statusString)) {
                statsDTO.setPendingAppointments(statsDTO.getPendingAppointments() + count);
            } else if ("CONFIRMED".equalsIgnoreCase(statusString)) {
                statsDTO.setConfirmedAppointments(statsDTO.getConfirmedAppointments() + count);
                if (paid != null && paid) {
                    statsDTO.setPaidConfirmedAppointments(statsDTO.getPaidConfirmedAppointments() + count);
                } else if (paid != null && !paid) {
                    statsDTO.setUnpaidConfirmedAppointments(statsDTO.getUnpaidConfirmedAppointments() + count);
                }
            } else if ("CANCELLED".equalsIgnoreCase(statusString)) {
                statsDTO.setCancelledAppointments(statsDTO.getCancelledAppointments() + count);
            }

            // Save back the updated DTO
            statsMap.put(date, statsDTO);
        }

        // Return a list of all statistics
        return new ArrayList<>(statsMap.values());
    }

    @Override
    public Admin getAdminById(String adminId) {
        return adminRepository.getReferenceById(adminId);
    }

}
