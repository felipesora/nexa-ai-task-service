package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.repository.WorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarWorkspacesPorIdUsuarioUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceControllerMapper mapper;

    public ListarWorkspacesPorIdUsuarioUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper) {
        this.workspaceRepository = workspaceRepository;
        this.mapper = mapper;
    }

    public Page<WorkspaceResponseDTO> execute(Long idUsuario, Pageable pageable) {
        return workspaceRepository.findByIdUsuario(idUsuario, pageable)
                .map(mapper::toResponse);
    }
}
