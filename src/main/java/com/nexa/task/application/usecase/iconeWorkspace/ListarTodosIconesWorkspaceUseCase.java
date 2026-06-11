package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.mapper.IconeWorkspaceControllerMapper;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodosIconesWorkspaceUseCase {

    private final IconeWorkspaceRepository repository;
    private final IconeWorkspaceControllerMapper mapper;

    public ListarTodosIconesWorkspaceUseCase(IconeWorkspaceRepository repository, IconeWorkspaceControllerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<IconeWorkspaceResponseDTO> execute(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
