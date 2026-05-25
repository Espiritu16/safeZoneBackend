package com.utp.safezonebackend.auth.dto.response;

import java.time.OffsetDateTime;

public record RefreshTokenResponse(
        String id,
        String usuarioId,
        OffsetDateTime expiraEn,
        boolean revocado,
        String revocadoPor,
        OffsetDateTime fechaRevocacion,
        OffsetDateTime fechaCreacion,
        boolean activo
) {
}
