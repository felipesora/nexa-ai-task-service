package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;

public class BuscarWorkspacePorIdUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceControllerMapper mapper;
    private final AuthenticationService authService;

    public BuscarWorkspacePorIdUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper, AuthenticationService authService) {
        this.workspaceRepository = workspaceRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public WorkspaceResponseDTO execute(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + id + " não encontrado."));

        authService.validateOwnerOrAdmin(workspace.getIdUsuario());

        return mapper.toResponse(workspace);
    }
}
