package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarWorkspacesPorIdUsuarioUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceControllerMapper mapper;
    private final AuthenticationService authService;

    public ListarWorkspacesPorIdUsuarioUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper, AuthenticationService authService) {
        this.workspaceRepository = workspaceRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public Page<WorkspaceResponseDTO> execute(Long idUsuario, Pageable pageable) {
        authService.validateOwnerOrAdmin(idUsuario);

        return workspaceRepository.findByIdUsuarioAndAtivo(idUsuario, pageable)
                .map(mapper::toResponse);
    }
}
