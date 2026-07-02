package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaCreateDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

public class CadastrarSubtarefaUseCase {

    private final SubtarefaRepository subtarefaRepository;
    private final TarefaRepository tarefaRepository;
    private final SubtarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public CadastrarSubtarefaUseCase(SubtarefaRepository subtarefaRepository, TarefaRepository tarefaRepository, SubtarefaControllerMapper mapper, AuthenticationService authService) {
        this.subtarefaRepository = subtarefaRepository;
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    @Transactional
    public SubtarefaResponseDTO execute(SubtarefaCreateDTO dto) {
        Tarefa tarefa = tarefaRepository.findByIdAtivo(dto.idTarefa())
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + dto.idTarefa() + " não encontrada"));

        authService.validateOwnerOrAdmin(tarefa.getIdUsuario());

        Subtarefa subtarefa = mapper.toDomain(dto, tarefa, tarefa.getIdUsuario());

        Subtarefa salvo = subtarefaRepository.save(subtarefa);
        return mapper.toResponse(salvo);
    }
}
