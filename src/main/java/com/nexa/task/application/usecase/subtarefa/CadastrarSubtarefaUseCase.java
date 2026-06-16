package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaRequestDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;

public class CadastrarSubtarefaUseCase {

    private final SubtarefaRepository subtarefaRepository;
    private final TarefaRepository tarefaRepository;
    private final SubtarefaControllerMapper mapper;

    public CadastrarSubtarefaUseCase(SubtarefaRepository subtarefaRepository, TarefaRepository tarefaRepository, SubtarefaControllerMapper mapper) {
        this.subtarefaRepository = subtarefaRepository;
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
    }

    public SubtarefaResponseDTO execute(SubtarefaRequestDTO request) {
        Tarefa tarefa = tarefaRepository.findById(request.idTarefa())
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + request.idTarefa() + " não encontrada"));

        Subtarefa salvo = subtarefaRepository.save(mapper.toDomain(request, tarefa));
        return mapper.toResponse(salvo);
    }
}
