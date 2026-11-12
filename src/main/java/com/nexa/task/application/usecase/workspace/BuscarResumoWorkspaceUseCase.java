package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.ResumoWorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;

public class BuscarResumoWorkspaceUseCase {

    private final WorkspaceRepository workspaceRepository;
    private final TarefaRepository tarefaRepository;
    private final AuthenticationService authService;

    public BuscarResumoWorkspaceUseCase(WorkspaceRepository workspaceRepository, TarefaRepository tarefaRepository, AuthenticationService authService) {
        this.workspaceRepository = workspaceRepository;
        this.tarefaRepository = tarefaRepository;
        this.authService = authService;
    }

    public ResumoWorkspaceResponseDTO execute(Long idWorkspace) {
        Workspace workspace = workspaceRepository.findByIdAtivo(idWorkspace)
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + idWorkspace + " não encontrado."));

        authService.validateOwnerOrAdmin(workspace.getIdUsuario());

        return new ResumoWorkspaceResponseDTO(
                tarefaRepository.countAtivasByIdWorkspace(idWorkspace),
                tarefaRepository.countAtivasByIdWorkspaceAndStatus(idWorkspace, StatusTarefa.CONCLUIDA),
                tarefaRepository.countAtivasByIdWorkspaceAndStatus(idWorkspace, StatusTarefa.EM_ANDAMENTO),
                tarefaRepository.countAtivasByIdWorkspaceAndStatus(idWorkspace, StatusTarefa.PENDENTE),
                tarefaRepository.countAtrasadasByIdWorkspace(idWorkspace)
        );
    }
}
