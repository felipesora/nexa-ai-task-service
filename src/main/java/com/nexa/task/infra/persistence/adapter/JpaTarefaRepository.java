package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;
import com.nexa.task.infra.persistence.mapper.TarefaPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataTarefaRepository;

public class JpaTarefaRepository implements TarefaRepository {

    private final SpringDataTarefaRepository repository;
    private final TarefaPersistenceMapper mapper;

    public JpaTarefaRepository(SpringDataTarefaRepository repository, TarefaPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Tarefa save(Tarefa tarefa) {
        TarefaEntity entity = mapper.toEntity(tarefa);
        repository.save(entity);
        return mapper.toDomain(entity);
    }
}
