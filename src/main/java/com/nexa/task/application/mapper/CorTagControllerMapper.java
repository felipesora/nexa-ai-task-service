package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.corTag.CorTagRequestDTO;
import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.domain.entity.tag.CorTag;

public class CorTagControllerMapper {

    public CorTag toDomain(CorTagRequestDTO request) {
        return new CorTag(
                null,
                request.cor(),
                true
        );
    }

    public CorTagResponseDTO toResponse(CorTag cor) {
        return new CorTagResponseDTO(
                cor.getId(),
                cor.getCor(),
                cor.getAtivo()
        );
    }
}
