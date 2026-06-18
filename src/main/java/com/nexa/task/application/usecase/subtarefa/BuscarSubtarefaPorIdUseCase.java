package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;

public class BuscarSubtarefaPorIdUseCase {

    private final SubtarefaRepository subtarefaRepository;
    private final SubtarefaControllerMapper mapper;

    public BuscarSubtarefaPorIdUseCase(SubtarefaRepository subtarefaRepository, SubtarefaControllerMapper mapper) {
        this.subtarefaRepository = subtarefaRepository;
        this.mapper = mapper;
    }

    public SubtarefaResponseDTO execute(Long idSubtarefa) {
        Subtarefa subtarefa = subtarefaRepository.findById(idSubtarefa)
                .orElseThrow(() -> new EntityNotFoundException("Subtarefa com id: " + idSubtarefa + " não encontrada."));

        return mapper.toResponse(subtarefa);
    }
}
