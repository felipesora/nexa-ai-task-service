package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;

public class DesativarIconeWorkspaceUseCase {

    private final IconeWorkspaceRepository repository;

    public DesativarIconeWorkspaceUseCase(IconeWorkspaceRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        IconeWorkspace icone = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ícone com id: " + id + " não encontrado."));

        icone.desativar();
        repository.save(icone);
    }
}
