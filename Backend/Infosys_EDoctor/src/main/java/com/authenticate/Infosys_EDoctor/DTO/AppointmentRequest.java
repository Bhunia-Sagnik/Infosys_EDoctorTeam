package com.authenticate.Infosys_EDoctor.DTO;

import lombok.Data;

@Data
public class AppointmentRequest {
    private String patientId = null;
    private String doctorId;                // ID of the doctor
    private String appointmentDateTime;  // Date and time of the appointment as a String
    private String reason;               // Reason for the appointment
}
