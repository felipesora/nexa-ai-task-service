package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;

public class DesativarTarefaUseCase {

    private final TarefaRepository tarefaRepository;
    private final AuthenticationService authService;

    public DesativarTarefaUseCase(TarefaRepository tarefaRepository, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.authService = authService;
    }

    public void execute(Long idTarefa) {
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada"));

        authService.validateOwnerOrAdmin(tarefa.getIdUsuario());

        tarefa.desativar();
        tarefaRepository.save(tarefa);
    }
}
