package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaRequestDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;

public class CadastrarTarefaUseCase {

    private final TarefaRepository tarefaRepository;
    private final WorkspaceRepository workspaceRepository;
    private final TarefaControllerMapper mapper;

    public CadastrarTarefaUseCase(TarefaRepository tarefaRepository, WorkspaceRepository workspaceRepository, TarefaControllerMapper mapper) {
        this.tarefaRepository = tarefaRepository;
        this.workspaceRepository = workspaceRepository;
        this.mapper = mapper;
    }

    public TarefaResponseDTO execute(TarefaRequestDTO request) {
        Workspace workspace = workspaceRepository.findById(request.idWorkspace())
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + request.idWorkspace() + " não encontrado"));

        Tarefa salvo = tarefaRepository.save(mapper.toDomain(request, workspace));
        return mapper.toResponse(salvo);
    }
}
