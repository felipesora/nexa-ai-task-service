package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.IconeWorkspaceControllerMapper;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;

public class BuscarIconeWorkspacePorIdUserCase {

    private final IconeWorkspaceRepository repository;
    private final IconeWorkspaceControllerMapper mapper;

    public BuscarIconeWorkspacePorIdUserCase(IconeWorkspaceRepository repository, IconeWorkspaceControllerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public IconeWorkspaceResponseDTO execute(Long id) {
        IconeWorkspace icone = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ícone com id: " + id + " não encontrado."));

        return mapper.toResponse(icone);
    }
}
