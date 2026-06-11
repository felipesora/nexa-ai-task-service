package com.nexa.task.application.usecase.corWorkspace;

import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.CorWorkspaceControllerMapper;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;

public class BuscarCorWorkspacePorIdUserCase {

    private final CorWorkspaceRepository repository;
    private final CorWorkspaceControllerMapper mapper;

    public BuscarCorWorkspacePorIdUserCase(CorWorkspaceRepository repository, CorWorkspaceControllerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CorWorkspaceResponseDTO execute(Long id) {
        CorWorkspace cor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cor do Workspace com id: " + id + " não encontrada."));

        return mapper.toResponse(cor);
    }
}
