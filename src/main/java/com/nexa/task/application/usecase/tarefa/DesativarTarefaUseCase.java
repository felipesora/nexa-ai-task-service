package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

import java.util.List;

public class DesativarTarefaUseCase {

    private final TarefaRepository tarefaRepository;
    private final SubtarefaRepository subtarefaRepository;
    private final AuthenticationService authService;

    public DesativarTarefaUseCase(TarefaRepository tarefaRepository, SubtarefaRepository subtarefaRepository, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.subtarefaRepository = subtarefaRepository;
        this.authService = authService;
    }

    @Transactional
    public void execute(Long idTarefa) {
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada"));

        authService.validateOwnerOrAdmin(tarefa.getIdUsuario());

        tarefa.desativar();

        List<Subtarefa> subtarefas = subtarefaRepository.findAllByTarefa(tarefa.getId());

        for (Subtarefa subtarefa : subtarefas) {
            subtarefa.desativar();
            subtarefaRepository.save(subtarefa);
        }

        tarefaRepository.save(tarefa);
    }
}
