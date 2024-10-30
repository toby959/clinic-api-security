package com.toby959.api.domain.doctor;

public record DataListDoctors(
        Long id,
        String name,
        String specialty,
        String document,
        String email
) {
    public DataListDoctors(Doctor doctor) {
        this(doctor.getId(), doctor.getName(), doctor.getSpecialty().toString(), doctor.getDocument(), doctor.getEmail());
    }
}
