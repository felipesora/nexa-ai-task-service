package com.nexa.task.application.usecase.corWorkspace;

import com.nexa.task.application.dto.corWorkspace.CorWorkspaceRequestDTO;
import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.mapper.CorWorkspaceControllerMapper;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;

import java.util.Optional;

public class CadastrarCorWorkspaceUseCase {

    private final CorWorkspaceRepository repository;
    private final CorWorkspaceControllerMapper mapper;

    public CadastrarCorWorkspaceUseCase(CorWorkspaceRepository repository, CorWorkspaceControllerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CorWorkspaceResponseDTO execute(CorWorkspaceRequestDTO request) {
        Optional<CorWorkspace> corExiste = repository.findByCor(request.cor());

        if (corExiste.isPresent()) {
            throw new BadRequestException("Esta cor de workspace já está cadastrada.");
        }

        CorWorkspace salvo = repository.save(mapper.toDomain(request));
        return mapper.toResponse(salvo);
    }
}
