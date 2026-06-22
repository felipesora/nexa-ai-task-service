package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.tag.TagCreateDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.entity.tag.Tag;

public class TagControllerMapper {

    private final CorTagControllerMapper corTagMapper;

    public TagControllerMapper(CorTagControllerMapper corTagMapper) {
        this.corTagMapper = corTagMapper;
    }

    public Tag toDomain(TagCreateDTO request, CorTag corTag) {
        return new Tag(
                null,
                request.idUsuario(),
                request.nome(),
                true,
                corTag
        );
    }

    public TagResponseDTO toResponse(Tag tag) {
        return new TagResponseDTO(
                tag.getId(),
                tag.getIdUsuario(),
                tag.getNome(),
                tag.getAtivo(),
                tag.getCorTag() != null ? corTagMapper.toResponse(tag.getCorTag()) : null
        );
    }
}
