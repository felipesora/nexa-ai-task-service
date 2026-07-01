package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;

public class BuscarSubtarefaPorIdUseCase {

    private final SubtarefaRepository subtarefaRepository;
    private final SubtarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public BuscarSubtarefaPorIdUseCase(SubtarefaRepository subtarefaRepository, SubtarefaControllerMapper mapper, AuthenticationService authService) {
        this.subtarefaRepository = subtarefaRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public SubtarefaResponseDTO execute(Long idSubtarefa) {
        Subtarefa subtarefa = subtarefaRepository.findById(idSubtarefa)
                .orElseThrow(() -> new EntityNotFoundException("Subtarefa com id: " + idSubtarefa + " não encontrada."));

        authService.validateOwnerOrAdmin(subtarefa.getIdUsuario());

        return mapper.toResponse(subtarefa);
    }
}
