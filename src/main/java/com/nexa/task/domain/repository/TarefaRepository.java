package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.tarefa.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TarefaRepository {

    Tarefa save(Tarefa tarefa);

    Page<Tarefa> findAll(Pageable pageable);

    Page<Tarefa> findByIdWorkspace(Long idWorkspace, Pageable pageable);

    Page<Tarefa> findByIdUsuario(Long idUsuario, Pageable pageable);

    Page<Tarefa> findByIdUsuarioAndTitulo(Long idUsuario, String titulo, Pageable pageable);

    Optional<Tarefa> findById(Long id);
}
