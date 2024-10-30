package com.toby959.api.domain.patient;

public record DataListPatient(Long id, String name, String email, String document) {

    public DataListPatient(Patient patient) {
        this(patient.getId(), patient.getName(), patient.getEmail(), patient.getDocument());
    }
}




