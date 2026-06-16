package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.subtarefa.SubtarefaCreateDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;

import java.time.LocalDateTime;

public class SubtarefaControllerMapper {

    public Subtarefa toDomain(SubtarefaCreateDTO request, Tarefa tarefa) {
        return new Subtarefa(
                null,
                request.titulo(),
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                tarefa
        );
    }

    public SubtarefaResponseDTO toResponse(Subtarefa subtarefa) {
        return new SubtarefaResponseDTO(
                subtarefa.getId(),
                subtarefa.getTitulo(),
                subtarefa.getConcluida(),
                subtarefa.getCriadoEm(),
                subtarefa.getAtualizadoEm(),
                subtarefa.getAtivo(),
                subtarefa.getTarefa().getId()
        );
    }
}
