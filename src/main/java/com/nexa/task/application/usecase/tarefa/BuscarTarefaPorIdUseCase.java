package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;

public class BuscarTarefaPorIdUseCase {

    private final TarefaRepository tarefaRepository;
    private final TarefaControllerMapper mapper;

    public BuscarTarefaPorIdUseCase(TarefaRepository tarefaRepository, TarefaControllerMapper mapper) {
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
    }

    public TarefaResponseDTO execute(Long idTarefa) {
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada."));

        return mapper.toResponse(tarefa);
    }
}
