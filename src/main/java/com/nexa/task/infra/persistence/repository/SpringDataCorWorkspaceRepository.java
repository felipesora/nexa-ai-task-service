package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.workspace.CorWorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataCorWorkspaceRepository extends JpaRepository<CorWorkspaceEntity, Long> {
}
