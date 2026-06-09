package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.mapper.IconeWorkspaceControllerMapper;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import jakarta.transaction.Transactional;

import java.util.Optional;

public class CadastrarIconeWorkspaceUseCase {

    private final IconeWorkspaceRepository repository;
    private final IconeWorkspaceControllerMapper mapper;

    public CadastrarIconeWorkspaceUseCase(IconeWorkspaceRepository repository, IconeWorkspaceControllerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public IconeWorkspaceResponseDTO execute(IconeWorkspaceRequestDTO request) {
        Optional<IconeWorkspace> nomeExiste = repository.findByNome(request.nome());

        if (nomeExiste.isPresent()) {
            throw new BadRequestException("Este nome de ícone já está cadastrado.");
        }

        Optional<IconeWorkspace> caminhoExiste = repository.findByCaminho(request.caminho());

        if (caminhoExiste.isPresent()) {
            throw new BadRequestException("Este caminho de ícone já está cadastrado.");
        }

        IconeWorkspace salvo = repository.save(mapper.toDomain(request));

        return mapper.toResponse(salvo);
    }
}
