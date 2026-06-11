package com.nexa.task.infra.persistence.mapper;

import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.infra.persistence.entity.workspace.IconeWorkspaceEntity;

public class IconeWorkspacePersistenceMapper {

    public IconeWorkspaceEntity toEntity(IconeWorkspace icone) {
        if (icone == null) {
            return null;
        }

        return new IconeWorkspaceEntity(
                icone.getId(),
                icone.getNome(),
                icone.getCaminho(),
                icone.getAtivo()
        );
    }

    public IconeWorkspace toDomain(IconeWorkspaceEntity entity) {
        if (entity == null) {
            return null;
        }

        return new IconeWorkspace(
                entity.getId(),
                entity.getNome(),
                entity.getCaminho(),
                entity.getAtivo()
        );
    }
}
