package com.nexa.task.infra.persistence.mapper;

import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.infra.persistence.entity.tag.TagEntity;

public class TagPersistenceMapper {

    private final CorTagPersistenceMapper corTagMapper;

    public TagPersistenceMapper(CorTagPersistenceMapper corTagMapper) {
        this.corTagMapper = corTagMapper;
    }

    public TagEntity toEntity(Tag tag) {
        if (tag == null) {
            return null;
        }

        return new TagEntity(
                tag.getId(),
                tag.getIdUsuario(),
                tag.getNome(),
                tag.getAtivo(),
                corTagMapper.toEntity(tag.getCorTag())
        );
    }

    public Tag toDomain(TagEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Tag(
                entity.getId(),
                entity.getIdUsuario(),
                entity.getNome(),
                entity.getAtivo(),
                corTagMapper.toDomain(entity.getCorTag())
        );
    }
}
