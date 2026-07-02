package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceUpdateDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class AtualizarWorkspaceUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final CorWorkspaceRepository corWorkspaceRepository;
    private final IconeWorkspaceRepository iconeWorkspaceRepository;
    private final AuthenticationService authService;

    public AtualizarWorkspaceUseCase(WorkspaceRepository workspaceRepository, CorWorkspaceRepository corWorkspaceRepository, IconeWorkspaceRepository iconeWorkspaceRepository, AuthenticationService authService) {
        this.workspaceRepository = workspaceRepository;
        this.corWorkspaceRepository = corWorkspaceRepository;
        this.iconeWorkspaceRepository = iconeWorkspaceRepository;
        this.authService = authService;
    }

    @Transactional
    public void execute(Long idWorkspace, WorkspaceUpdateDTO updateDTO) {
        Workspace workspace = workspaceRepository.findByIdAtivo(idWorkspace)
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + idWorkspace + " não encontrado"));

        authService.validateOwnerOrAdmin(workspace.getIdUsuario());

        validarNomeUnicoDeWorkspace(workspace, updateDTO);

        CorWorkspace corWorkspace = null;
        IconeWorkspace iconeWorkspace = null;

        if (updateDTO.idCor() != null) {
            corWorkspace = corWorkspaceRepository.findByIdAtivo(updateDTO.idCor())
                    .orElseThrow(() -> new EntityNotFoundException("Cor com id: " + updateDTO.idCor() + " não encontrada"));
        }

        if (updateDTO.idIcone() != null) {
            iconeWorkspace = iconeWorkspaceRepository.findByIdAtivo(updateDTO.idIcone())
                    .orElseThrow(() -> new EntityNotFoundException("Ícone com id: " + updateDTO.idIcone() + " não encontrado"));
        }

        workspace.setNome(updateDTO.nome());
        workspace.setDescricao(updateDTO.descricao());
        workspace.setAtualizadoEm(LocalDateTime.now());
        workspace.setCorWorkspace(corWorkspace);
        workspace.setIconeWorkspace(iconeWorkspace);

        workspaceRepository.save(workspace);
    }

    private void validarNomeUnicoDeWorkspace(Workspace workspace, WorkspaceUpdateDTO updateDTO) {
        if (workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(updateDTO.nome(), workspace.getIdUsuario(), workspace.getId())) {
            throw new BadRequestException("Já existe um workspace com esse nome para este usuário");
        }
    }
}
