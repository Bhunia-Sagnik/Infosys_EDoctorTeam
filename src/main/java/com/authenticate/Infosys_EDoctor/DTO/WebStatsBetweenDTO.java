package com.authenticate.Infosys_EDoctor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebStatsBetweenDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private int totalAppointments;
    private int pendingAppointments;
    private int confirmedAppointments;
    private int cancelledAppointments;

    private int paidConfirmedAppointments;
    private int unpaidConfirmedAppointments;
}
