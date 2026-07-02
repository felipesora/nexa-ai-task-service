package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;

public class DesmarcarSubtarefaConcluidaUseCase {

    private final SubtarefaRepository subtarefaRepository;
    private final AuthenticationService authService;

    public DesmarcarSubtarefaConcluidaUseCase(SubtarefaRepository subtarefaRepository, AuthenticationService authService) {
        this.subtarefaRepository = subtarefaRepository;
        this.authService = authService;
    }

    public void execute(Long idSubtarefa) {
        Subtarefa subtarefa = subtarefaRepository.findByIdAtivo(idSubtarefa)
                .orElseThrow(() -> new EntityNotFoundException("Subtarefa com id: " + idSubtarefa + " não encontrada"));

        authService.validateOwnerOrAdmin(subtarefa.getIdUsuario());

        subtarefa.desmarcarConcluida();
        subtarefaRepository.save(subtarefa);
    }
}
