package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.tarefa.TarefaCreateDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;

import java.time.LocalDateTime;

public class TarefaControllerMapper {

    public Tarefa toDomain(TarefaCreateDTO request, Workspace workspace) {

        //deixar tags null por enquanto
        return new Tarefa(
                null,
                request.idUsuario(),
                request.titulo(),
                request.descricao(),
                request.prioridade(),
                StatusTarefa.EM_ANDAMENTO,
                request.dificuldade(),
                request.dataLimite(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                workspace,
                null
        );
    }

    public TarefaResponseDTO toResponse(Tarefa tarefa) {
        return new TarefaResponseDTO(
                tarefa.getId(),
                tarefa.getIdUsuario(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getPrioridade(),
                tarefa.getStatus(),
                tarefa.getDificuldade(),
                tarefa.getDataLimite(),
                tarefa.getDataConclusao(),
                tarefa.getCriadoEm(),
                tarefa.getAtualizadoEm(),
                tarefa.getAtivo(),
                tarefa.getWorkspace().getId()
        );
    }
}
