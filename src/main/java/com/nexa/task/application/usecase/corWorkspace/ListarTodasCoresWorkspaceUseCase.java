package com.nexa.task.application.usecase.corWorkspace;

import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.mapper.CorWorkspaceControllerMapper;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodasCoresWorkspaceUseCase {

    private final CorWorkspaceRepository repository;
    private final CorWorkspaceControllerMapper mapper;

    public ListarTodasCoresWorkspaceUseCase(CorWorkspaceRepository repository, CorWorkspaceControllerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<CorWorkspaceResponseDTO> execute(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
