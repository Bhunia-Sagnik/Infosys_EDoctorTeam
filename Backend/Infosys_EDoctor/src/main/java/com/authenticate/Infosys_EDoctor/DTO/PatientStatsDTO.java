package com.authenticate.Infosys_EDoctor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientStatsDTO {
    private String patientId;
    private String name;
    private String email;
    private String mobileNo;
    private long totalAppointments;
    private long pendingAppointments;
    private long cancelledAppointments;
    private long confirmedAppointments;
    private long paidAppointments;
    private long unpaidAppointments;
}
