package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.workspace.IconeWorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataIconeWorkspaceRepository extends JpaRepository<IconeWorkspaceEntity, Long> {
}
