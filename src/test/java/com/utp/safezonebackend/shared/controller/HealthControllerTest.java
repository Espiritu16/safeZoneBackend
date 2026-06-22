package com.utp.safezonebackend.shared.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;

class HealthControllerTest {

    @Test
    void healthDevuelveEstadoOk() {
        HealthController controller = new HealthController();

        Map<String, String> response = controller.health();

        assertThat(response).containsEntry("status", "ok");
        assertThat(response).containsEntry("application", "SafeZone-Backend");
    }
}
