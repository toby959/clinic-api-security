package com.toby959.api.domain.doctor;

import com.toby959.api.domain.address.DataAddress;
import jakarta.validation.constraints.NotNull;

public record DataUpDateDoctor(
        @NotNull Long id,
        String name,
        String document,
        String email,
        DataAddress address
) {
}
