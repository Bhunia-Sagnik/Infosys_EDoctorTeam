package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.authenticate.Infosys_EDoctor.Service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class ReminderSchedulerTest {

    @Autowired
    private ReminderScheduler reminderScheduler;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private NotificationService notificationService;

    private Appointment confirmedAppointment;
    private Appointment pendingAppointment;

    @BeforeEach
    void setUp() {
        confirmedAppointment = new Appointment();
        confirmedAppointment.setAppointmentId(1L);
        confirmedAppointment.setStatus(Appointment.Status.Confirmed);
        confirmedAppointment.setAppointmentDateTime(LocalDateTime.now().plusDays(1));

        pendingAppointment = new Appointment();
        pendingAppointment.setAppointmentId(2L);
        pendingAppointment.setStatus(Appointment.Status.Pending);
        pendingAppointment.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
    }

    @Test
    void testScheduleNextDayReminders_ConfirmedAppointments() {
        // Mock the service call
        when(appointmentService.getAppointmentsByDate(LocalDate.now().plusDays(1)))
                .thenReturn(Collections.singletonList(confirmedAppointment));

        // Execute the scheduler method
        reminderScheduler.scheduleNextDayReminders();

        // Verify that the notification service was called
        verify(notificationService, times(1)).sendAppointmentReminder(confirmedAppointment);
        // Verify that no notification was sent for other statuses
        verify(notificationService, never()).sendAppointmentReminder(pendingAppointment);
    }

    @Test
    void testScheduleNextDayReminders_NoAppointments() {
        // Mock the service call to return an empty list
        when(appointmentService.getAppointmentsByDate(LocalDate.now().plusDays(1)))
                .thenReturn(Collections.emptyList());

        // Execute the scheduler method
        reminderScheduler.scheduleNextDayReminders();

        // Verify that the notification service was never called
        verify(notificationService, never()).sendAppointmentReminder(any(Appointment.class));
    }

    @Test
    void testScheduleNextDayReminders_MixedAppointments() {
        // Mock the service call with both confirmed and pending appointments
        List<Appointment> appointments = Arrays.asList(confirmedAppointment, pendingAppointment);
        when(appointmentService.getAppointmentsByDate(LocalDate.now().plusDays(1)))
                .thenReturn(appointments);

        // Execute the scheduler method
        reminderScheduler.scheduleNextDayReminders();

        // Verify notification is sent only for confirmed appointment
        verify(notificationService, times(1)).sendAppointmentReminder(confirmedAppointment);
        verify(notificationService, never()).sendAppointmentReminder(pendingAppointment);
    }
}
