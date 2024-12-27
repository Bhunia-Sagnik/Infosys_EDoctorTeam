package com.authenticate.Infosys_EDoctor.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "doctor_availability")
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int availabilityId;

    @NotNull(message = "Doctor ID cannot be null")
    private String doctorId;

    @NotNull(message = "From date cannot be null")
    private LocalDate fromDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;
}

