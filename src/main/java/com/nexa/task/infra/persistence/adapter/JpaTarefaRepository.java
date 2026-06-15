package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;
import com.nexa.task.infra.persistence.mapper.TarefaPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataTarefaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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

    @Override
    public Page<Tarefa> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tarefa> findByIdWorkspace(Long idWorkspace, Pageable pageable) {
        return repository.findByWorkspace_Id(idWorkspace, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tarefa> findByIdUsuario(Long idUsuario, Pageable pageable) {
        return repository.findByIdUsuario(idUsuario, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tarefa> findByIdUsuarioAndTitulo(Long idUsuario, String titulo, Pageable pageable) {
        return repository.findByIdUsuarioAndTituloContainingIgnoreCase(idUsuario, titulo, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Tarefa> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
}
