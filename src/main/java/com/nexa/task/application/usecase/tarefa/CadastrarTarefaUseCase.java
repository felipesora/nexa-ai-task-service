package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaCreateDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;

public class CadastrarTarefaUseCase {

    private final TarefaRepository tarefaRepository;
    private final WorkspaceRepository workspaceRepository;
    private final TarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public CadastrarTarefaUseCase(TarefaRepository tarefaRepository, WorkspaceRepository workspaceRepository, TarefaControllerMapper mapper, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.workspaceRepository = workspaceRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public TarefaResponseDTO execute(TarefaCreateDTO dto) {
        Workspace workspace = workspaceRepository.findByIdAtivo(dto.idWorkspace())
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + dto.idWorkspace() + " não encontrado"));

        authService.validateOwnerOrAdmin(workspace.getIdUsuario());

        Tarefa tarefa = mapper.toDomain(dto, workspace, workspace.getIdUsuario());

        Tarefa salvo = tarefaRepository.save(tarefa);
        return mapper.toResponse(salvo);
    }
}
