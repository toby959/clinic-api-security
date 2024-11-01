package com.toby959.api.domain.patient;

import com.toby959.api.domain.address.DataAddress;

public record PatientResponseData(
        Long id,
        String name,
        String email,
        String phone,
        String document,
        DataAddress address
) {
}
