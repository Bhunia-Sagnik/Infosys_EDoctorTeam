package com.authenticate.Infosys_EDoctor.Repository;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByDoctorId(String doctorId);
    List<Doctor> findBySpecialization(String specialization);

    Doctor findByEmail(String email);
}
