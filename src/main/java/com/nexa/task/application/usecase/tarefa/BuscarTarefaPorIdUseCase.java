package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;

public class BuscarTarefaPorIdUseCase {

    private final TarefaRepository tarefaRepository;
    private final TarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public BuscarTarefaPorIdUseCase(TarefaRepository tarefaRepository, TarefaControllerMapper mapper, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public TarefaResponseDTO execute(Long idTarefa) {
        Tarefa tarefa = tarefaRepository.findByIdAtivo(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada."));

        authService.validateOwnerOrAdmin(tarefa.getIdUsuario());

        return mapper.toResponse(tarefa);
    }
}
