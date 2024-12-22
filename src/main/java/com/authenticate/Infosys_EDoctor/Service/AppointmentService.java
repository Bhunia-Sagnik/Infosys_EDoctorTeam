package com.authenticate.Infosys_EDoctor.Service;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface AppointmentService {
    Appointment scheduleAppointment(Appointment appointment);
    List<Appointment> getAppointmentsForPatient(String patientId);
    List<Appointment> getAppointmentsForDoctor(String doctorId);

    Appointment getAppointmentById(Long appointmentId);

    Appointment updateAppointment(Long appointmentId, Appointment updatedAppointment);

    void cancelAppointment(Long appointmentId, String reason);

    Appointment confirmAppointment(Long appointmentId);

    List<Appointment> getAppointmentsByDate(LocalDate nextDay);

    List<Appointment> getAllAppointments();

    List<Appointment> findAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    List<Object[]> getAppointmentsGroupedByDate();
}
