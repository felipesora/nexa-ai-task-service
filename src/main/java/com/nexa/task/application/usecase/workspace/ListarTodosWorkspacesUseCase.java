package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.repository.WorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodosWorkspacesUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceControllerMapper mapper;

    public ListarTodosWorkspacesUseCase(WorkspaceRepository workspaceRepository, WorkspaceControllerMapper mapper) {
        this.workspaceRepository = workspaceRepository;
        this.mapper = mapper;
    }

    public Page<WorkspaceResponseDTO> execute(Pageable pageable) {
        return workspaceRepository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
