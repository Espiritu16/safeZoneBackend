package com.utp.safezonebackend.victimas.mapper;

import com.utp.safezonebackend.victimas.dto.response.VictimaAliasResponse;
import com.utp.safezonebackend.victimas.entity.VictimaAlias;
import org.springframework.stereotype.Component;

@Component
public class VictimaAliasMapper {

    public VictimaAliasResponse toResponse(VictimaAlias entity) {
        return new VictimaAliasResponse();
    }
}
