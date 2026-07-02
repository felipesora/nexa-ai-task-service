package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTarefasPorIdWorkspaceUseCase {

    private final TarefaRepository tarefaRepository;
    private final WorkspaceRepository workspaceRepository;
    private final TarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public ListarTarefasPorIdWorkspaceUseCase(TarefaRepository tarefaRepository, WorkspaceRepository workspaceRepository, TarefaControllerMapper mapper, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.workspaceRepository = workspaceRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public Page<TarefaResponseDTO> execute(Long idWorkspace, Pageable pageable) {
        Workspace workspace = workspaceRepository.findByIdAtivo(idWorkspace)
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + idWorkspace + " não encontrado."));

        authService.validateOwnerOrAdmin(workspace.getIdUsuario());

        return tarefaRepository.findByIdWorkspace(idWorkspace, pageable)
                .map(mapper::toResponse);
    }
}
