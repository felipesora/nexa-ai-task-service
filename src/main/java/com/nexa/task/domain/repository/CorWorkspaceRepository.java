package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.workspace.CorWorkspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CorWorkspaceRepository {

    CorWorkspace save(CorWorkspace cor);

    Page<CorWorkspace> findAll(Pageable pageable);

    Optional<CorWorkspace> findById(Long id);
}
