package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceRequestDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class AtualizarWorkspaceUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final CorWorkspaceRepository corWorkspaceRepository;
    private final IconeWorkspaceRepository iconeWorkspaceRepository;

    public AtualizarWorkspaceUseCase(WorkspaceRepository workspaceRepository, CorWorkspaceRepository corWorkspaceRepository, IconeWorkspaceRepository iconeWorkspaceRepository) {
        this.workspaceRepository = workspaceRepository;
        this.corWorkspaceRepository = corWorkspaceRepository;
        this.iconeWorkspaceRepository = iconeWorkspaceRepository;
    }

    @Transactional
    public void execute(Long idWorkspace, WorkspaceRequestDTO request) {
        Workspace workspace = workspaceRepository.findById(idWorkspace)
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + idWorkspace + " não encontrado"));

        validarNomeUnicoDeWorkspace(idWorkspace, request);

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

        workspace.setIdUsuario(request.idUsuario());
        workspace.setNome(request.nome());
        workspace.setDescricao(request.descricao());
        workspace.setAtualizadoEm(LocalDateTime.now());
        workspace.setCorWorkspace(corWorkspace);
        workspace.setIconeWorkspace(iconeWorkspace);

        workspaceRepository.save(workspace);
    }

    private void validarNomeUnicoDeWorkspace(Long idWorkspace, WorkspaceRequestDTO request) {
        if (workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(request.nome(), request.idUsuario(), idWorkspace)) {
            throw new BadRequestException("Já existe um workspace com esse nome para este usuário");
        }
    }
}
