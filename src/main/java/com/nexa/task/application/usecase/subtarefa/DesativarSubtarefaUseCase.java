package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;

public class DesativarSubtarefaUseCase {

    private final SubtarefaRepository subtarefaRepository;

    public DesativarSubtarefaUseCase(SubtarefaRepository subtarefaRepository) {
        this.subtarefaRepository = subtarefaRepository;
    }

    public void execute(Long idSubtarefa) {
        Subtarefa subtarefa = subtarefaRepository.findById(idSubtarefa)
                .orElseThrow(() -> new EntityNotFoundException("Subtarefa com id: " + idSubtarefa + " não encontrada"));

        subtarefa.desativar();
        subtarefaRepository.save(subtarefa);
    }
}
