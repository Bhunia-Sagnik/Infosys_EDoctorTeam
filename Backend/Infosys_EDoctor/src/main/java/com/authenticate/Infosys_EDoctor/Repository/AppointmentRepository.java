package com.authenticate.Infosys_EDoctor.Repository;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Appointment> findByPatient_PatientId(String patientId);
    List<Appointment> findByDoctor_DoctorId(String doctorId);
    Optional<Appointment> findByPaymentId(String paymentId);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime >= :startDate AND a.appointmentDateTime <= :endDate")
    List<Appointment> findAppointmentsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DATE_FORMAT(a.appointmentDateTime, '%Y-%m-%d') AS date, a.status, a.paid, COUNT(a) " +
            "FROM Appointment a " +
            "GROUP BY DATE_FORMAT(a.appointmentDateTime, '%Y-%m-%d'), a.status, a.paid " +
            "ORDER BY date ASC")
    List<Object[]> findAppointmentsGroupedByDate();
}
