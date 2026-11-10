package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.tarefa.TarefaCreateDTO;
import com.nexa.task.application.dto.tarefa.TarefaPrioritariaResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.SubtarefaRepository;

import java.time.LocalDateTime;

public class TarefaControllerMapper {

    private final SubtarefaRepository subtarefaRepository;

    public TarefaControllerMapper(SubtarefaRepository subtarefaRepository) {
        this.subtarefaRepository = subtarefaRepository;
    }

    public Tarefa toDomain(TarefaCreateDTO request, Workspace workspace, Long idUsuario) {

        return new Tarefa(
                null,
                idUsuario,
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
                tarefa.getWorkspace().getId(),
                subtarefaRepository.countAtivasByIdTarefa(tarefa.getId()),
                subtarefaRepository.countConcluidasAtivasByIdTarefa(tarefa.getId())
        );
    }

    public TarefaPrioritariaResponseDTO toPrioritariaResponse(Tarefa tarefa) {
        return new TarefaPrioritariaResponseDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getPrioridade(),
                tarefa.getStatus(),
                tarefa.getDataLimite(),
                tarefa.getWorkspace().getId(),
                tarefa.getWorkspace().getNome()
        );
    }
}
