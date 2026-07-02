package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SubtarefaRepository {

    Subtarefa save(Subtarefa subtarefa);

    Page<Subtarefa> findAll(Pageable pageable);

    Page<Subtarefa> findByIdTarefa(Long idTarefa, Pageable pageable);

    List<Subtarefa> findAllByTarefa(Long idTarefa);

    Optional<Subtarefa> findById(Long id);

    Optional<Subtarefa> findByIdAtivo(Long id);
}
