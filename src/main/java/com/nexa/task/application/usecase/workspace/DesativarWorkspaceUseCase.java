package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

import java.util.List;

public class DesativarWorkspaceUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final TarefaRepository tarefaRepository;
    private final SubtarefaRepository subtarefaRepository;
    private final AuthenticationService authService;

    public DesativarWorkspaceUseCase(WorkspaceRepository workspaceRepository, TarefaRepository tarefaRepository, SubtarefaRepository subtarefaRepository, AuthenticationService authService) {
        this.workspaceRepository = workspaceRepository;
        this.tarefaRepository = tarefaRepository;
        this.subtarefaRepository = subtarefaRepository;
        this.authService = authService;
    }

    @Transactional
    public void execute(Long idWorkspace) {
        Workspace workspace = workspaceRepository.findById(idWorkspace)
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + idWorkspace + " não encontrado"));

        authService.validateOwnerOrAdmin(workspace.getIdUsuario());

        workspace.desativar();

        List<Tarefa> tarefas = tarefaRepository.findAllByWorkspace(idWorkspace);

        for (Tarefa tarefa : tarefas) {
            tarefa.desativar();

            List<Subtarefa> subtarefas = subtarefaRepository.findAllByTarefa(tarefa.getId());

            for (Subtarefa subtarefa : subtarefas) {
                subtarefa.desativar();
                subtarefaRepository.save(subtarefa);
            }

            tarefaRepository.save(tarefa);
        }

        workspaceRepository.save(workspace);
    }
}
