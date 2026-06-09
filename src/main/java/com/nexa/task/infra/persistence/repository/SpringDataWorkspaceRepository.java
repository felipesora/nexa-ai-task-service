package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.workspace.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataWorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {
}
