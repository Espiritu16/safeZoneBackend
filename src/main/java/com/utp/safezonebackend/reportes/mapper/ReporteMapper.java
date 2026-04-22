package com.utp.safezonebackend.reportes.mapper;

import com.utp.safezonebackend.reportes.dto.response.ReporteMensualResponse;
import org.springframework.stereotype.Component;

@Component
public class ReporteMapper {

    public ReporteMensualResponse toResponse() {
        return new ReporteMensualResponse();
    }
}
