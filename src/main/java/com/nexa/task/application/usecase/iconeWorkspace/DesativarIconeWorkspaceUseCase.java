package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;

public class DesativarIconeWorkspaceUseCase {

    private final IconeWorkspaceRepository repository;
    private final WorkspaceRepository workspaceRepository;

    public DesativarIconeWorkspaceUseCase(IconeWorkspaceRepository repository, WorkspaceRepository workspaceRepository) {
        this.repository = repository;
        this.workspaceRepository = workspaceRepository;
    }

    @Transactional
    public void execute(Long id) {
        IconeWorkspace icone = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ícone com id: " + id + " não encontrado."));

        workspaceRepository.removerIconeDosWorkspaces(id);

        icone.desativar();
        repository.save(icone);
    }
}
