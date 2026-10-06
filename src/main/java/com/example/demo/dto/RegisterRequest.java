package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Firstname required")
        @Size(min = 2, message = "Firstname must be at least 2 characters long")
        String firstName,

        @NotBlank(message = "Lastname required")
        @Size(min = 2, message = "Lastname must be at least 2 characters long")
        String lastName,

        @NotBlank(message = "Email required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_\\-*]).{8,20}$",
                message = "Password must be between 8 and 20 characters long and include at least " +
                        "one uppercase letter, one lowercase letter, one digit, and one special character (@#$%^&+=!_-*)"
        )
        String password,

        @NotBlank(message = "Phone number required")
        @Pattern(
                regexp = "^\\+[1-9]\\d{6,14}$",
                message = "Phone number must be in international format (e.g., +380501234567)"
        )
        String phoneNumber
) {
    @Override
    public String toString() {
        return "RegisterRequest[firstName=" + firstName + ", lastName=" + lastName +
                ", email=" + email + ", phoneNumber=" + phoneNumber + ", password=****]";
    }
}
