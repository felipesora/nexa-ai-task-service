package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.workspace.Workspace;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    boolean existsByNomeAndIdUsuario(String nome, Long idUsuario);
}
