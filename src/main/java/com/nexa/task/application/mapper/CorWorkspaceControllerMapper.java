package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.corWorkspace.CorWorkspaceRequestDTO;
import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.domain.entity.workspace.CorWorkspace;

public class CorWorkspaceControllerMapper {

    public CorWorkspace toDomain(CorWorkspaceRequestDTO request) {
        return new CorWorkspace(
                null,
                request.cor(),
                true
        );
    }

    public CorWorkspaceResponseDTO toResponse(CorWorkspace cor) {
        return new CorWorkspaceResponseDTO(
                cor.getId(),
                cor.getCor(),
                cor.getAtivo()
        );
    }
}
