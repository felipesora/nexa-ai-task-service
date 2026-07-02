package com.nexa.task.infra.persistence.mapper;

import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.infra.persistence.entity.subtarefa.SubtarefaEntity;

public class SubtarefaPersistenceMapper {

    private final TarefaPersistenceMapper tarefaMapper;

    public SubtarefaPersistenceMapper(TarefaPersistenceMapper tarefaMapper) {
        this.tarefaMapper = tarefaMapper;
    }

    public SubtarefaEntity toEntity(Subtarefa subtarefa) {
        if (subtarefa == null) {
            return null;
        }

        return new SubtarefaEntity(
                subtarefa.getId(),
                subtarefa.getIdUsuario(),
                subtarefa.getTitulo(),
                subtarefa.getConcluida(),
                subtarefa.getCriadoEm(),
                subtarefa.getAtualizadoEm(),
                subtarefa.getAtivo(),
                tarefaMapper.toEntity(subtarefa.getTarefa())
        );
    }

    public Subtarefa toDomain(SubtarefaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Subtarefa(
                entity.getId(),
                entity.getIdUsuario(),
                entity.getTitulo(),
                entity.getConcluida(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm(),
                entity.getAtivo(),
                tarefaMapper.toDomain(entity.getTarefa())
        );
    }
}
