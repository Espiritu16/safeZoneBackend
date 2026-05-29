package com.utp.safezonebackend.predenuncias.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DescartarPreDenunciaRequest(
        @NotBlank @Size(max = 255) String motivoDescarte
) {
}
