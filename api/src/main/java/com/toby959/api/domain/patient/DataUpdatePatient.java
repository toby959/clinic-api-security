package com.toby959.api.domain.patient;

import com.toby959.api.domain.address.DataAddress;
import jakarta.validation.constraints.NotNull;

public record DataUpdatePatient(
        @NotNull
        Long id,
        String name,
        String phone,
        DataAddress address
) {
}




