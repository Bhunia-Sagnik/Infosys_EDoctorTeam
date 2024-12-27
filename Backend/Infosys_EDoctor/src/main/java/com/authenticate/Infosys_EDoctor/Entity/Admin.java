package com.authenticate.Infosys_EDoctor.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    String adminId;

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
}
