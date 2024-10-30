package com.toby959.api.domain.doctor;

import com.toby959.api.domain.address.DataAddress;

public record DoctorResponseData(
        Long id,
        String name,
        String email,
        String phone,
        String document,
        DataAddress address
) {
}
