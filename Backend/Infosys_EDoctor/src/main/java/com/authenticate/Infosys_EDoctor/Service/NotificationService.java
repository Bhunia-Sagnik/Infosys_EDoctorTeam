package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface NotificationService {
    public void sendDoctorProfileCreatedNotification(String email, String doctorId);
    public void sendPatientProfileCreatedNotification(String email, String patientId);
    public void sendAdminProfileCreatedNotification(String email, String adminId);
    public void sendNewAppointmentNotificationToDoctor(Appointment appointment);

    void sendUpdatedAppointmentNotificationToPatient(Appointment appointment);

    public void sendAppointmentConfirmationToPatient(Appointment appointment);
    public void sendAppointmentCancellationNotification(Long appointmentId, String reason, boolean notifyPatient);
    public void sendAppointmentReminder(Appointment appointment);

    void sendProfileUpdatedByAdminNotification(String email, String id);

    void sendAdminProfileUpdatedNotification(String email, String adminId);

    void sendAdminProfileDeletedNotification(String email, String adminId);

    void sendProfileDeletedByAdminNotification(@NotBlank(message = "Email is mandatory") @Email(message = "Invalid email address") String email, String patientId);

    void sendNewAppointmentNotificationToPatient(Appointment appointment);
}
