package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.infra.persistence.entity.workspace.CorWorkspaceEntity;
import com.nexa.task.infra.persistence.mapper.CorWorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataCorWorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JpaCorWorkspaceRepository implements CorWorkspaceRepository {

    private final SpringDataCorWorkspaceRepository corWorkspaceRepository;
    private final CorWorkspacePersistenceMapper mapper;

    public JpaCorWorkspaceRepository(SpringDataCorWorkspaceRepository corWorkspaceRepository, CorWorkspacePersistenceMapper mapper) {
        this.corWorkspaceRepository = corWorkspaceRepository;
        this.mapper = mapper;
    }

    @Override
    public CorWorkspace save(CorWorkspace cor) {
        CorWorkspaceEntity entity = mapper.toEntity(cor);
        corWorkspaceRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Page<CorWorkspace> findAll(Pageable pageable) {
        return corWorkspaceRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CorWorkspace> findById(Long id) {
        return corWorkspaceRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CorWorkspace> findByCor(String cor) {
        return corWorkspaceRepository.findByCor(cor)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CorWorkspace> findByIdAtivo(Long id) {
        return corWorkspaceRepository.findByIdAndAtivoTrue(id)
                .map(mapper::toDomain);
    }
}
