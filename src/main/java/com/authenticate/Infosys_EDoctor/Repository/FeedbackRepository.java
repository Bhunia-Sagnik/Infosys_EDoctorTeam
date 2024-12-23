package com.authenticate.Infosys_EDoctor.Repository;

import com.authenticate.Infosys_EDoctor.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Find feedback by doctor ID
    @Query("SELECT f FROM Feedback f WHERE f.doctor.id = :doctorId")
    List<Feedback> findByDoctorId(@Param("doctorId") String doctorId);

    // Find feedback by patient ID
    @Query("SELECT f FROM Feedback f WHERE f.patient.id = :patientId")
    List<Feedback> findByPatientId(@Param("patientId") String patientId);

    // Find average rating for a doctor
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.doctor.id = :doctorId")
    Double findAverageRatingByDoctorId(@Param("doctorId") String doctorId);

    // Find distinct doctor IDs for a patient
    @Query("SELECT DISTINCT f.doctor.id FROM Feedback f WHERE f.patient.id = :patientId")
    List<String> findDoctorIdsByPatientId(@Param("patientId") String patientId);
}
