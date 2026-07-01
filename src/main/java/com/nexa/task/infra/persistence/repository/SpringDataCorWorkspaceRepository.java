package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.workspace.CorWorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataCorWorkspaceRepository extends JpaRepository<CorWorkspaceEntity, Long> {

    Optional<CorWorkspaceEntity> findByCor(String cor);

    Optional<CorWorkspaceEntity> findByIdAndAtivoTrue(Long id);
}
