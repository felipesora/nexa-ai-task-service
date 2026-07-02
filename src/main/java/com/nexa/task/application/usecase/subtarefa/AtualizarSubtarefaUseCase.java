package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaUpdateDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class AtualizarSubtarefaUseCase {

    private final SubtarefaRepository repository;
    private final AuthenticationService authService;

    public AtualizarSubtarefaUseCase(SubtarefaRepository repository, AuthenticationService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    @Transactional
    public void execute(Long idSubtarefa, SubtarefaUpdateDTO dto) {
        Subtarefa subtarefa = repository.findByIdAtivo(idSubtarefa)
                .orElseThrow(() -> new EntityNotFoundException("Subtarefa com id: " + idSubtarefa + " não encontrada."));

        authService.validateOwnerOrAdmin(subtarefa.getIdUsuario());

        subtarefa.setTitulo(dto.titulo());
        subtarefa.setAtualizadoEm(LocalDateTime.now());
        repository.save(subtarefa);
    }
}
