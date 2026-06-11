package com.nexa.task.infra.persistence.mapper;

import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.infra.persistence.entity.workspace.WorkspaceEntity;

public class WorkspacePersistenceMapper {

    private final CorWorkspacePersistenceMapper corWorkspaceMapper;
    private final IconeWorkspacePersistenceMapper iconeWorkspaceMapper;

    public WorkspacePersistenceMapper(CorWorkspacePersistenceMapper corWorkspaceMapper, IconeWorkspacePersistenceMapper iconeWorkspaceMapper) {
        this.corWorkspaceMapper = corWorkspaceMapper;
        this.iconeWorkspaceMapper = iconeWorkspaceMapper;
    }

    public WorkspaceEntity toEntity(Workspace workspace) {
        if (workspace == null) {
            return null;
        }

        return new WorkspaceEntity(
                workspace.getId(),
                workspace.getIdUsuario(),
                workspace.getNome(),
                workspace.getDescricao(),
                workspace.getCriadoEm(),
                workspace.getAtualizadoEm(),
                workspace.getAtivo(),
                corWorkspaceMapper.toEntity(workspace.getCorWorkspace()),
                iconeWorkspaceMapper.toEntity(workspace.getIconeWorkspace())
        );
    }

    public Workspace toDomain(WorkspaceEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Workspace(
                entity.getId(),
                entity.getIdUsuario(),
                entity.getNome(),
                entity.getDescricao(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getAtivo(),
                corWorkspaceMapper.toDomain(entity.getCorWorkspace()),
                iconeWorkspaceMapper.toDomain(entity.getIconeWorkspace())
        );
    }
}
