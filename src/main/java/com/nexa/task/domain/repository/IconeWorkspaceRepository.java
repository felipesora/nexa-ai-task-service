package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IconeWorkspaceRepository {

    IconeWorkspace save(IconeWorkspace icone);

    Page<IconeWorkspace> findAll(Pageable pageable);

    Optional<IconeWorkspace> findById(Long id);
}
