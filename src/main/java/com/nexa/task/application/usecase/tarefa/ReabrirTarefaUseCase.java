package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class ReabrirTarefaUseCase {

    private final TarefaRepository tarefaRepository;

    public ReabrirTarefaUseCase(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    @Transactional
    public void execute(Long idTarefa) {
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada"));

        if (tarefa.getStatus() != StatusTarefa.CONCLUIDA) {
            throw new BadRequestException("Somente tarefas concluídas podem ser reabertas.");
        }

        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa.setDataConclusao(null);
        tarefa.setAtualizadoEm(LocalDateTime.now());

        tarefaRepository.save(tarefa);
    }
}
