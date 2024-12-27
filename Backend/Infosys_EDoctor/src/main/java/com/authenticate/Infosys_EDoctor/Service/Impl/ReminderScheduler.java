package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.authenticate.Infosys_EDoctor.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReminderScheduler {

    @Autowired
    private AppointmentService appointmentService; // Service to fetch appointments

    @Autowired
    private NotificationService notificationService;

    // Runs every day at 8 AM
    @Scheduled(cron = "0 50 13 * * ?")
    public void scheduleNextDayReminders() {
        LocalDate nextDay = LocalDate.now().plusDays(1);
        List<Appointment> appointments = appointmentService.getAppointmentsByDate(nextDay);

        for (Appointment appointment : appointments) {
            if(appointment.getStatus() == Appointment.Status.Confirmed) {
                notificationService.sendAppointmentReminder(appointment);
            }
        }
    }
}

