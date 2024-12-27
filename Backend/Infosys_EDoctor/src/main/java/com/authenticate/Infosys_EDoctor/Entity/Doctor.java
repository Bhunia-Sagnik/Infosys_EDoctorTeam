package com.authenticate.Infosys_EDoctor.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    private String doctorId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String specialization;

    @NotBlank(message = "Location is mandatory")
    private String location;

    private String hospitalName;

    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must consist of 10 digits")
    @Column(length = 10)
    private String mobileNo;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email address")
    @Column(unique = true) // Ensures no duplicate email
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull
    private int chargedPerVisit;
}