package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.infra.persistence.entity.tag.CorTagEntity;
import com.nexa.task.infra.persistence.mapper.CorTagPersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataCorTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JpaCorTagRepository implements CorTagRepository {

    private final SpringDataCorTagRepository corTagRepository;
    private final CorTagPersistenceMapper mapper;

    public JpaCorTagRepository(SpringDataCorTagRepository corTagRepository, CorTagPersistenceMapper mapper) {
        this.corTagRepository = corTagRepository;
        this.mapper = mapper;
    }

    @Override
    public CorTag save(CorTag cor) {
        CorTagEntity entity = mapper.toEntity(cor);
        corTagRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Page<CorTag> findAll(Pageable pageable) {
        return corTagRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CorTag> findById(Long id) {
        return corTagRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CorTag> findByCor(String cor) {
        return corTagRepository.findByCor(cor)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CorTag> findByIdAtivo(Long id) {
        return corTagRepository.findByIdAndAtivoTrue(id)
                .map(mapper::toDomain);
    }
}
