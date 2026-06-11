package com.nexa.task.application.usecase.corWorkspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;

public class DesativarCorWorkspaceUseCase {

    private final CorWorkspaceRepository repository;

    public DesativarCorWorkspaceUseCase(CorWorkspaceRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        CorWorkspace cor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cor do Workspace com id: " + id + " não encontrada."));

        cor.desativar();
        repository.save(cor);
    }
}
