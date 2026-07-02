package com.nexa.task.application.usecase.corWorkspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;

public class DesativarCorWorkspaceUseCase {

    private final CorWorkspaceRepository repository;
    private final WorkspaceRepository workspaceRepository;

    public DesativarCorWorkspaceUseCase(CorWorkspaceRepository repository, WorkspaceRepository workspaceRepository) {
        this.repository = repository;
        this.workspaceRepository = workspaceRepository;
    }

    @Transactional
    public void execute(Long id) {
        CorWorkspace cor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cor do Workspace com id: " + id + " não encontrada."));

        workspaceRepository.removerCorDosWorkspaces(id);

        cor.desativar();
        repository.save(cor);
    }
}
