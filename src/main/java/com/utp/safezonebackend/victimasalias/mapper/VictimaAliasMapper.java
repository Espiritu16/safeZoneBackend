package com.utp.safezonebackend.victimasalias.mapper;

import com.utp.safezonebackend.victimasalias.dto.response.VictimaAliasResponse;
import com.utp.safezonebackend.victimasalias.entity.VictimaAlias;
import org.springframework.stereotype.Component;

@Component
public class VictimaAliasMapper {

    public VictimaAliasResponse toResponse(VictimaAlias entity) {
        return new VictimaAliasResponse();
    }
}
