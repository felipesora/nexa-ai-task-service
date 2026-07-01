package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.workspace.IconeWorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataIconeWorkspaceRepository extends JpaRepository<IconeWorkspaceEntity, Long> {

    Optional<IconeWorkspaceEntity> findByNome(String nome);

    Optional<IconeWorkspaceEntity> findByCaminho(String caminho);

    Optional<IconeWorkspaceEntity> findByIdAndAtivoTrue(Long id);
}
