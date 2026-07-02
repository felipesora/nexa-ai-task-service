package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.tarefa.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TarefaRepository {

    Tarefa save(Tarefa tarefa);

    Page<Tarefa> findAll(Pageable pageable);

    Page<Tarefa> findByIdWorkspace(Long idWorkspace, Pageable pageable);

    Page<Tarefa> findByIdWorkspaceAndAtivo(Long idWorkspace, Pageable pageable);

    List<Tarefa> findAllByWorkspace(Long idWorkspace);

    Page<Tarefa> findByIdUsuario(Long idUsuario, Pageable pageable);

    Page<Tarefa> findByIdUsuarioAndAtivo(Long idUsuario, Pageable pageable);

    Page<Tarefa> findByIdUsuarioAndTitulo(Long idUsuario, String titulo, Pageable pageable);

    Page<Tarefa> findByIdUsuarioAndTituloAndAtivo(Long idUsuario, String titulo, Pageable pageable);

    Optional<Tarefa> findById(Long id);

    Optional<Tarefa> findByIdAtivo(Long id);
}
