package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.ResumoTarefasResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaPrioritariaResponseDTO;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;

import java.util.List;

public class BuscarResumoTarefasPorIdUsuarioUseCase {

    static final int LIMITE_PRIORITARIAS = 5;

    private final TarefaRepository tarefaRepository;
    private final TarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public BuscarResumoTarefasPorIdUsuarioUseCase(TarefaRepository tarefaRepository, TarefaControllerMapper mapper, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public ResumoTarefasResponseDTO execute(Long idUsuario) {
        authService.validateOwnerOrAdmin(idUsuario);

        List<TarefaPrioritariaResponseDTO> prioritarias = tarefaRepository
                .findPrioritariasByIdUsuario(idUsuario, LIMITE_PRIORITARIAS)
                .stream()
                .map(mapper::toPrioritariaResponse)
                .toList();

        return new ResumoTarefasResponseDTO(
                tarefaRepository.countAtivasByIdUsuarioAndStatus(idUsuario, StatusTarefa.CONCLUIDA),
                tarefaRepository.countAtivasByIdUsuarioAndStatus(idUsuario, StatusTarefa.EM_ANDAMENTO),
                tarefaRepository.countAtivasByIdUsuarioAndStatus(idUsuario, StatusTarefa.PENDENTE),
                tarefaRepository.countAtrasadasByIdUsuario(idUsuario),
                prioritarias
        );
    }
}
