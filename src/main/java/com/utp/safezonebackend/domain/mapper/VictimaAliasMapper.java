package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.VictimaAliasResponse;
import com.utp.safezonebackend.persistance.entity.VictimaAlias;
import org.springframework.stereotype.Component;

@Component
public class VictimaAliasMapper {

    public VictimaAliasResponse toResponse(VictimaAlias entity) {
        return new VictimaAliasResponse();
    }
}
