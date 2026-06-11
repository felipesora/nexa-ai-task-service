package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.workspace.WorkspaceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataWorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {

    boolean existsByNomeAndIdUsuario(String nome, Long idUsuario);

    boolean existsByNomeAndIdUsuarioAndIdNot(String nome, Long idUsuario, Long id);

    Page<WorkspaceEntity> findByIdUsuario(Long idUsuario, Pageable pageable);

    Page<WorkspaceEntity> findByIdUsuarioAndNomeContainingIgnoreCase(Long idUsuario, String nome, Pageable pageable);
}
