package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.workspace.WorkspaceRequestDTO;
import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.entity.workspace.Workspace;

import java.time.LocalDateTime;

public class WorkspaceControllerMapper {

    private final CorWorkspaceControllerMapper corWorkspaceMapper;
    private final IconeWorkspaceControllerMapper iconeWorkspaceMapper;

    public WorkspaceControllerMapper(CorWorkspaceControllerMapper corWorkspaceMapper, IconeWorkspaceControllerMapper iconeWorkspaceMapper) {
        this.corWorkspaceMapper = corWorkspaceMapper;
        this.iconeWorkspaceMapper = iconeWorkspaceMapper;
    }

    public Workspace toDomain(WorkspaceRequestDTO request, CorWorkspace cor, IconeWorkspace icone, Long idUsuario) {
        return new Workspace(
             null,
                idUsuario,
                request.nome(),
                request.descricao(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                cor,
                icone
        );
    }

    public WorkspaceResponseDTO toResponse(Workspace workspace) {
        return new WorkspaceResponseDTO(
                workspace.getId(),
                workspace.getIdUsuario(),
                workspace.getNome(),
                workspace.getDescricao(),
                workspace.getCriadoEm(),
                workspace.getAtualizadoEm(),
                workspace.getAtivo(),
                workspace.getCorWorkspace() != null ? corWorkspaceMapper.toResponse(workspace.getCorWorkspace()) : null,
                workspace.getIconeWorkspace() != null ? iconeWorkspaceMapper.toResponse(workspace.getIconeWorkspace()) : null
        );
    }
}
