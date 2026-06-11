package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.WorkspaceRepository;

public class BuscarWorkspacePorIdUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceControllerMapper mapper;

    public BuscarWorkspacePorIdUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper) {
        this.workspaceRepository = workspaceRepository;
        this.mapper = mapper;
    }

    public WorkspaceResponseDTO execute(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + id + " não encontrado."));

        return mapper.toResponse(workspace);
    }
}
