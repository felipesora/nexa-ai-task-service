package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceRequestDTO;
import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticatedUser;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

public class CadastrarWorkspaceUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final CorWorkspaceRepository corWorkspaceRepository;
    private final IconeWorkspaceRepository iconeWorkspaceRepository;
    private final WorkspaceControllerMapper mapper;
    private final AuthenticationService authService;

    public CadastrarWorkspaceUseCase(WorkspaceRepository workspaceRepository, CorWorkspaceRepository corWorkspaceRepository, IconeWorkspaceRepository iconeWorkspaceRepository, WorkspaceControllerMapper mapper, AuthenticationService authService) {
        this.workspaceRepository = workspaceRepository;
        this.corWorkspaceRepository = corWorkspaceRepository;
        this.iconeWorkspaceRepository = iconeWorkspaceRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    @Transactional
    public WorkspaceResponseDTO execute(WorkspaceRequestDTO request) {

        authService.validateOwnerOrAdmin(request.idUsuario());

        validarNomeUnicoDeWorkspace(request);

        CorWorkspace corWorkspace = null;
        IconeWorkspace iconeWorkspace = null;

        if (request.idCor() != null) {
            corWorkspace = corWorkspaceRepository.findById(request.idCor())
                    .orElseThrow(() -> new EntityNotFoundException("Cor com id: " + request.idCor() + " não encontrada"));
        }

        if (request.idIcone() != null) {
            iconeWorkspace = iconeWorkspaceRepository.findById(request.idIcone())
                    .orElseThrow(() -> new EntityNotFoundException("Ícone com id: " + request.idIcone() + " não encontrado"));
        }

        Workspace salvo = workspaceRepository.save(mapper.toDomain(request, corWorkspace, iconeWorkspace));
        return mapper.toResponse(salvo);
    }

    private void validarNomeUnicoDeWorkspace(WorkspaceRequestDTO request) {
        if (workspaceRepository.existsByNomeAndIdUsuario(request.nome(), request.idUsuario())) {
            throw new BadRequestException("Já existe um workspace com esse nome para este usuário");
        }
    }
}
