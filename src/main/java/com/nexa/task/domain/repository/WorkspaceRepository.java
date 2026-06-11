package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.workspace.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    Page<Workspace> findAll(Pageable pageable);

    Page<Workspace> findByIdUsuario(Long idUsuario, Pageable pageable);

    Page<Workspace> findByIdUsuarioAndNome(Long idUsuario, String nome, Pageable pageable);

    Optional<Workspace> findById(Long id);

    boolean existsByNomeAndIdUsuario(String nome, Long idUsuario);
}
