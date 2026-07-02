package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class IniciarTarefaUseCase {

    private final TarefaRepository tarefaRepository;
    private final AuthenticationService authService;

    public IniciarTarefaUseCase(TarefaRepository tarefaRepository, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.authService = authService;
    }

    @Transactional
    public void execute(Long idTarefa) {
        Tarefa tarefa = tarefaRepository.findByIdAtivo(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada"));

        authService.validateOwnerOrAdmin(tarefa.getIdUsuario());

        if (tarefa.getStatus() == StatusTarefa.EM_ANDAMENTO) {
            throw new BadRequestException("A tarefa já está em andamento.");
        }

        if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
            throw new BadRequestException("Tarefas concluídas devem ser reabertas antes de serem iniciadas.");
        }

        tarefa.setStatus(StatusTarefa.EM_ANDAMENTO);
        tarefa.setDataConclusao(null);
        tarefa.setAtualizadoEm(LocalDateTime.now());

        tarefaRepository.save(tarefa);
    }
}
