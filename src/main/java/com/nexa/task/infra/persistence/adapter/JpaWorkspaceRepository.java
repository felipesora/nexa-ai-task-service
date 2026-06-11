package com.nexa.task.infra.persistence.adapter;

import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.persistence.entity.workspace.WorkspaceEntity;
import com.nexa.task.infra.persistence.mapper.WorkspacePersistenceMapper;
import com.nexa.task.infra.persistence.repository.SpringDataWorkspaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JpaWorkspaceRepository implements WorkspaceRepository {

    private final SpringDataWorkspaceRepository repository;
    private final WorkspacePersistenceMapper mapper;

    public JpaWorkspaceRepository(SpringDataWorkspaceRepository repository, WorkspacePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Workspace save(Workspace workspace) {
        WorkspaceEntity entity = mapper.toEntity(workspace);
        repository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Page<Workspace> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Workspace> findByIdUsuario(Long idUsuario, Pageable pageable) {
        return repository.findByIdUsuario(idUsuario, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Workspace> findByIdUsuarioAndNome(Long idUsuario, String nome, Pageable pageable) {
        return repository.findByIdUsuarioAndNomeContainingIgnoreCase(idUsuario, nome, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Workspace> findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByNomeAndIdUsuario(String nome, Long idUsuario) {
        return repository.existsByNomeAndIdUsuario(nome, idUsuario);
    }
}
