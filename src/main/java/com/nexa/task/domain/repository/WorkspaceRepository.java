package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.workspace.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    Page<Workspace> findAll(Pageable pageable);

    boolean existsByNomeAndIdUsuario(String nome, Long idUsuario);
}
