package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.infra.persistence.entity.tag.TagEntity;
import com.nexa.task.infra.persistence.mapper.TagPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JpaTagRepository implements TagRepository {

    private final SpringDataTagRepository repository;
    private final TagPersistenceMapper mapper;

    public JpaTagRepository(SpringDataTagRepository repository, TagPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Tag save(Tag tag) {
        TagEntity entity = mapper.toEntity(tag);
        repository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tag> findByIdTarefa(Long idTarefa, Pageable pageable) {
        return repository.buscarTagsPorIdTarefa(idTarefa, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tag> findByIdUsuario(Long idUsuario, Pageable pageable) {
        return repository.findByIdUsuario(idUsuario, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByNomeAndIdUsuario(String nome, Long idUsuario) {
        return repository.existsByNomeAndIdUsuario(nome, idUsuario);
    }

    @Override
    public boolean existsByNomeAndIdUsuarioAndIdNot(String nome, Long idUsuario, Long id) {
        return repository.existsByNomeAndIdUsuarioAndIdNot(nome, idUsuario, id);
    }

    @Override
    public void removerCorDasTags(Long idCor) {
        repository.removerCorDasTags(idCor);
    }
}
