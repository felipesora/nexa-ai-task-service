package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ListarTarefasAgendaUseCase {

    // A visão mensal do calendário cobre no máximo 6 semanas; 62 dias dá folga sem permitir buscas enormes
    private static final long MAXIMO_DIAS_PERIODO = 62;

    private final TarefaRepository tarefaRepository;
    private final TarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public ListarTarefasAgendaUseCase(TarefaRepository tarefaRepository, TarefaControllerMapper mapper, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public List<TarefaResponseDTO> execute(Long idUsuario, LocalDate inicio, LocalDate fim) {
        authService.validateOwnerOrAdmin(idUsuario);

        if (!fim.isAfter(inicio)) {
            throw new BadRequestException("A data de fim deve ser posterior a data de inicio.");
        }
        if (ChronoUnit.DAYS.between(inicio, fim) > MAXIMO_DIAS_PERIODO) {
            throw new BadRequestException("O periodo da agenda nao pode ser maior que " + MAXIMO_DIAS_PERIODO + " dias.");
        }

        // fim é exclusivo: a busca vai até o último instante do dia anterior
        return tarefaRepository.findAtivasByIdUsuarioAndDataLimiteEntre(
                        idUsuario, inicio.atStartOfDay(), fim.atStartOfDay().minusNanos(1))
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
