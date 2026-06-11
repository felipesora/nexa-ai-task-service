package com.nexa.task.infra.persistence.mapper;

import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.infra.persistence.entity.workspace.CorWorkspaceEntity;

public class CorWorkspacePersistenceMapper {

    public CorWorkspaceEntity toEntity(CorWorkspace cor) {
        if (cor == null) {
            return null;
        }

        return new CorWorkspaceEntity(
                cor.getId(),
                cor.getCor(),
                cor.getAtivo()
        );
    }

    public CorWorkspace toDomain(CorWorkspaceEntity entity) {
        if (entity == null) {
            return null;
        }

        return new CorWorkspace(
                entity.getId(),
                entity.getCor(),
                entity.getAtivo()
        );
    }
}
