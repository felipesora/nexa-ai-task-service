package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarSubtarefasPorIdTarefaUseCase {

    private final SubtarefaRepository subtarefaRepository;
    private final TarefaRepository tarefaRepository;
    private final SubtarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public ListarSubtarefasPorIdTarefaUseCase(SubtarefaRepository subtarefaRepository, TarefaRepository tarefaRepository, SubtarefaControllerMapper mapper, AuthenticationService authService) {
        this.subtarefaRepository = subtarefaRepository;
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public Page<SubtarefaResponseDTO> execute(Long idTarefa, Pageable pageable) {
        Tarefa tarefa = tarefaRepository.findByIdAtivo(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada."));

        authService.validateOwnerOrAdmin(tarefa.getIdUsuario());

        return subtarefaRepository.findByIdTarefa(idTarefa, pageable)
                .map(mapper::toResponse);
    }
}
