package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTarefasPorIdUsuarioETituloUseCase {

    private final TarefaRepository tarefaRepository;
    private final TarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public ListarTarefasPorIdUsuarioETituloUseCase(TarefaRepository tarefaRepository, TarefaControllerMapper mapper, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public Page<TarefaResponseDTO> execute(Long idUsuario, String titulo, Pageable pageable) {
        authService.validateOwnerOrAdmin(idUsuario);

        return tarefaRepository.findByIdUsuarioAndTituloAndAtivo(idUsuario, titulo, pageable)
                .map(mapper::toResponse);
    }
}
