package com.nexa.task.infra.persistence.mapper;

import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TarefaPersistenceMapper {

    private final WorkspacePersistenceMapper workspaceMapper;
    private final TagPersistenceMapper tagMapper;

    public TarefaPersistenceMapper(WorkspacePersistenceMapper workspaceMapper, TagPersistenceMapper tagMapper) {
        this.workspaceMapper = workspaceMapper;
        this.tagMapper = tagMapper;
    }

    public TarefaEntity toEntity(Tarefa tarefa) {
        if (tarefa == null) {
            return null;
        }

        TarefaEntity entity = new TarefaEntity(
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

        entity.setTags(
                tarefa.getTags()
                        .stream()
                        .map(tagMapper::toEntity)
                        .collect(Collectors.toSet())
        );

        return entity;
    }

    public Tarefa toDomain(TarefaEntity entity) {
        if (entity == null) {
            return null;
        }

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
                entity.getTags()
                        .stream()
                        .map(tagMapper::toDomain)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
