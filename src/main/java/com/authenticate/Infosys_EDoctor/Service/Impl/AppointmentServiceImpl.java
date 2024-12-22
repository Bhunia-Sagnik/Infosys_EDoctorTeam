package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Repository.AppointmentRepository;
import com.authenticate.Infosys_EDoctor.Repository.DoctorAvailabilityRepository;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    @Override
    public Appointment scheduleAppointment(Appointment appointment) {
        // Check doctor availability
        List<DoctorAvailability> availabilities = availabilityRepository.findByDoctorId(
                appointment.getDoctor().getDoctorId()
        );

        boolean isSlotAvailable = availabilities.stream().anyMatch(availability ->
                appointment.getAppointmentDateTime().toLocalDate().isAfter(availability.getFromDate().minusDays(1)) &&
                        appointment.getAppointmentDateTime().toLocalDate().isBefore(availability.getEndDate().plusDays(1))
        );

        if (!isSlotAvailable) {
            throw new RuntimeException("Doctor is not available for the selected time slot");
        }

        // Save appointment
        appointment.setStatus(Appointment.Status.Pending);
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsForPatient(String patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    @Override
    public List<Appointment> getAppointmentsForDoctor(String doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId);
    }

    @Override
    public Appointment getAppointmentById(Long appointmentId) {
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + appointmentId));

        return existingAppointment;
    }

    @Override
    @Transactional
    public Appointment updateAppointment(Long appointmentId, Appointment updatedAppointment) {
        Appointment existingAppointment = getAppointmentById(appointmentId);

        existingAppointment.setAppointmentDateTime(updatedAppointment.getAppointmentDateTime());
        existingAppointment.setReason(updatedAppointment.getReason());
        existingAppointment.setStatus(Appointment.Status.Pending);


        return appointmentRepository.save(existingAppointment);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId, String reason) {
        Appointment existingAppointment = getAppointmentById(appointmentId);

        if (existingAppointment.getStatus() == Appointment.Status.Cancelled) {
            throw new IllegalStateException("Appointment is already cancelled.");
        }
        if (existingAppointment.getStatus() == Appointment.Status.Confirmed) {
            throw new IllegalStateException("Confirmed appointments cannot be cancelled.");
        }


        existingAppointment.setStatus(Appointment.Status.Cancelled);
        existingAppointment.setReason(reason);

        appointmentRepository.save(existingAppointment);
    }

    @Override
    public Appointment confirmAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);

        appointment.setStatus(Appointment.Status.Confirmed);

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByAppointmentDateTimeBetween(
                date.atStartOfDay(),
                date.atTime(23, 59, 59)
        );
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> findAppointmentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentRepository.findAppointmentsBetweenDates(startDate, endDate);
    }

    @Override
    public List<Object[]> getAppointmentsGroupedByDate() {
        return appointmentRepository.findAppointmentsGroupedByDate();
    }


}
