package com.toby959.api.domain.doctor;


import com.toby959.api.domain.address.DataAddress;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MedicalRecordData(
                                @NotBlank
                                String name,
                                @NotBlank
                                @Email
                                String email,
                                @NotBlank
                                String phone,
                                @NotBlank
                                @Pattern(regexp = "\\d{4,6}")
                                String document,
                                @NotNull
                                Specialty specialty,
                                @NotNull
                                @Valid
                                DataAddress address
) {
}
