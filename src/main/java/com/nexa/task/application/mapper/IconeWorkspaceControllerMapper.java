package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;

public class IconeWorkspaceControllerMapper {

    public IconeWorkspace toDomain(IconeWorkspaceRequestDTO request) {
        return new IconeWorkspace(
                null,
                request.nome(),
                request.caminho(),
                true
        );
    }

    public IconeWorkspaceResponseDTO toResponse(IconeWorkspace icone) {
        return new IconeWorkspaceResponseDTO(
                icone.getId(),
                icone.getNome(),
                icone.getCaminho(),
                icone.getAtivo()
        );
    }
}
