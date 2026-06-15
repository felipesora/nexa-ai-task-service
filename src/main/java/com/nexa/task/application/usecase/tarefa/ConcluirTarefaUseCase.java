package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class ConcluirTarefaUseCase {

    private final TarefaRepository tarefaRepository;

    public ConcluirTarefaUseCase(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    @Transactional
    public void execute(Long idTarefa) {
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada"));

        if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
            throw new BadRequestException("A tarefa já está concluída.");
        }

        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefa.setDataConclusao(LocalDateTime.now());
        tarefa.setAtualizadoEm(LocalDateTime.now());

        tarefaRepository.save(tarefa);
    }
}
