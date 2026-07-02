package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.workspace.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    Page<Workspace> findAll(Pageable pageable);

    Page<Workspace> findByIdUsuario(Long idUsuario, Pageable pageable);

    Page<Workspace> findByIdUsuarioAndNome(Long idUsuario, String nome, Pageable pageable);

    List<Workspace> findAllByIdIconeWorkspace(Long idIconeWorkspace);

    List<Workspace> findAllByIdCorWorkspace(Long idCorWorkspace);

    Optional<Workspace> findById(Long id);

    Optional<Workspace> findByIdAtivo(Long id);

    boolean existsByNomeAndIdUsuario(String nome, Long idUsuario);

    boolean existsByNomeAndIdUsuarioAndIdNot(String nome, Long idUsuario, Long id);

    void removerIconeDosWorkspaces(Long idIcone);

    void removerCorDosWorkspaces(Long idCor);
}
