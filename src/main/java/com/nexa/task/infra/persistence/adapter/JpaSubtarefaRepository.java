package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.infra.persistence.entity.subtarefa.SubtarefaEntity;
import com.nexa.task.infra.persistence.mapper.SubtarefaPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataSubtarefaRepository;

public class JpaSubtarefaRepository implements SubtarefaRepository {

    private final SpringDataSubtarefaRepository repository;
    private final SubtarefaPersistenceMapper mapper;

    public JpaSubtarefaRepository(SpringDataSubtarefaRepository repository, SubtarefaPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Subtarefa save(Subtarefa subtarefa) {
        SubtarefaEntity entity = mapper.toEntity(subtarefa);
        repository.save(entity);
        return mapper.toDomain(entity);
    }
}
