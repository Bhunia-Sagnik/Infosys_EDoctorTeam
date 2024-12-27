package com.authenticate.Infosys_EDoctor.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
@Table(name = "patients")
public class Patient {
    public enum Gender {
        MALE, FEMALE, OTHERS
    }

    @Id
    private String patientId;

    @NotBlank(message = "Name is mandatory")
    private String name;

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

    private String bloodGroup;

    @NotNull(message = "Gender is mandatory")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Min(value = 1)
    private int age;

    @NotBlank
    private String address;
}