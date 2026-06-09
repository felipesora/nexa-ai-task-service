package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.infra.persistence.entity.workspace.IconeWorkspaceEntity;
import com.nexa.task.infra.persistence.mapper.IconeWorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataIconeWorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JpaIconeWorkspaceRepository implements IconeWorkspaceRepository {

    private final SpringDataIconeWorkspaceRepository iconeWorkspaceRepository;
    private final IconeWorkspacePersistenceMapper mapper;

    public JpaIconeWorkspaceRepository(SpringDataIconeWorkspaceRepository iconeWorkspaceRepository, IconeWorkspacePersistenceMapper mapper) {
        this.iconeWorkspaceRepository = iconeWorkspaceRepository;
        this.mapper = mapper;
    }

    @Override
    public IconeWorkspace save(IconeWorkspace icone) {
        IconeWorkspaceEntity entity = mapper.toEntity(icone);
        iconeWorkspaceRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Page<IconeWorkspace> findAll(Pageable pageable) {
        return iconeWorkspaceRepository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<IconeWorkspace> findById(Long id) {
        return iconeWorkspaceRepository.findById(id)
                .map(mapper::toDomain);
    }
}
