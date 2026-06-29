package com.utp.safezonebackend.evidencias.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class CrearEvidenciaRequest {
    private MultipartFile file;

    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
}
