package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.WorkspaceRepository;

public class AtivarWorkspaceUseCase {

    private final WorkspaceRepository workspaceRepository;

    public AtivarWorkspaceUseCase(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public void execute(Long idWorkspace) {
        Workspace workspace = workspaceRepository.findById(idWorkspace)
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + idWorkspace + " não encontrado"));

        if (workspaceRepository.existsAtivoByNomeAndIdUsuarioAndIdNot(workspace.getNome(), workspace.getIdUsuario(), workspace.getId())) {
            throw new BadRequestException("Já existe um workspace ativo com esse nome para este usuário");
        }

        workspace.ativar();
        workspaceRepository.save(workspace);
    }
}
