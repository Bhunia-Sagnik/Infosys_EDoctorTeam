package com.authenticate.Infosys_EDoctor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorStatsDTO {
    private String doctorId;
    private String name;
    private String email;
    private String mobileNo;
    private String specialization;
    private long totalAppointments;
    private long pendingAppointments;
    private long cancelledAppointments;
    private long confirmedAppointments;
    private long paidAppointments;
    private long unpaidAppointments;
}
