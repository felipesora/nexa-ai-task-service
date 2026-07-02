package com.nexa.task.infra.persistence.mapper;

import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.infra.persistence.entity.tag.CorTagEntity;

public class CorTagPersistenceMapper {

    public CorTagEntity toEntity(CorTag cor) {
        if (cor == null) {
            return null;
        }

        return new CorTagEntity(
                cor.getId(),
                cor.getCor(),
                cor.getAtivo()
        );
    }

    public CorTag toDomain(CorTagEntity entity) {
        if (entity == null) {
            return null;
        }

        return new CorTag(
                entity.getId(),
                entity.getCor(),
                entity.getAtivo()
        );
    }
}
