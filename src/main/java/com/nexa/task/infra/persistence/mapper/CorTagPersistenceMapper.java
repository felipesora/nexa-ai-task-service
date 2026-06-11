package com.nexa.task.infra.persistence.mapper;

import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.infra.persistence.entity.tag.CorTagEntity;

public class CorTagPersistenceMapper {

    public CorTagEntity toEntity(CorTag cor) {
        return new CorTagEntity(
                cor.getId(),
                cor.getCor(),
                cor.getAtivo()
        );
    }

    public CorTag toDomain(CorTagEntity entity) {
        return new CorTag(
                entity.getId(),
                entity.getCor(),
                entity.getAtivo()
        );
    }
}
