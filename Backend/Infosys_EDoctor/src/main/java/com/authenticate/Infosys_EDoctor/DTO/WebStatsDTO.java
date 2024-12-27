package com.authenticate.Infosys_EDoctor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebStatsDTO {
    private String date;
    private long totalAppointments;
    private long pendingAppointments;
    private long confirmedAppointments;
    private long cancelledAppointments;
    private long paidConfirmedAppointments;
    private long unpaidConfirmedAppointments;
}
