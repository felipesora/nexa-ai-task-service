package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaUpdateDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class AtualizarSubtarefaUseCase {

    private final SubtarefaRepository repository;

    public AtualizarSubtarefaUseCase(SubtarefaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long idSubtarefa, SubtarefaUpdateDTO dto) {
        Subtarefa subtarefa = repository.findById(idSubtarefa)
                .orElseThrow(() -> new EntityNotFoundException("Subtarefa com id: " + idSubtarefa + " não encontrada."));

        subtarefa.setTitulo(dto.titulo());
        subtarefa.setAtualizadoEm(LocalDateTime.now());
        repository.save(subtarefa);
    }
}
