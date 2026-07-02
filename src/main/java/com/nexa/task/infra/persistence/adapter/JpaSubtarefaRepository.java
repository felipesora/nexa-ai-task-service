package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.infra.persistence.entity.subtarefa.SubtarefaEntity;
import com.nexa.task.infra.persistence.mapper.SubtarefaPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataSubtarefaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Page<Subtarefa> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Subtarefa> findByIdTarefa(Long idTarefa, Pageable pageable) {
        return repository.findByTarefa_Id(idTarefa, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public List<Subtarefa> findAllByTarefa(Long idTarefa) {
        return repository.findByTarefa_Id(idTarefa)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Subtarefa> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Subtarefa> findByIdAtivo(Long id) {
        return repository.findByIdAndAtivoTrue(id)
                .map(mapper::toDomain);
    }
}
