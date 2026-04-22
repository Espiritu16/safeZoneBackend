package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.ReporteMensualResponse;
import org.springframework.stereotype.Component;

@Component
public class ReporteMapper {

    public ReporteMensualResponse toResponse() {
        return new ReporteMensualResponse();
    }
}
