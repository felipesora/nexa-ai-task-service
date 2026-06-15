package com.nexa.task.infra.persistence.mapper;

import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;

public class TarefaPersistenceMapper {

    private final WorkspacePersistenceMapper workspaceMapper;

    public TarefaPersistenceMapper(WorkspacePersistenceMapper workspaceMapper) {
        this.workspaceMapper = workspaceMapper;
    }

    public TarefaEntity toEntity(Tarefa tarefa) {
        if (tarefa == null) {
            return null;
        }

        return new TarefaEntity(
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
                workspaceMapper.toEntity(tarefa.getWorkspace())
        );
    }

    public Tarefa toDomain(TarefaEntity entity) {
        if (entity == null) {
            return null;
        }

        // por enquanto deixar null as tags
        return new Tarefa(
                entity.getId(),
                entity.getIdUsuario(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getPrioridade(),
                entity.getStatus(),
                entity.getDificuldade(),
                entity.getDataLimite(),
                entity.getDataConclusao(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getAtivo(),
                workspaceMapper.toDomain(entity.getWorkspace()),
                null
        );
    }
}
