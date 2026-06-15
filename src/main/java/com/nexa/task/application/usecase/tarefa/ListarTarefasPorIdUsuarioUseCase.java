package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.repository.TarefaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTarefasPorIdUsuarioUseCase {

    private final TarefaRepository tarefaRepository;
    private final TarefaControllerMapper mapper;

    public ListarTarefasPorIdUsuarioUseCase(TarefaRepository tarefaRepository, TarefaControllerMapper mapper) {
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
    }

    public Page<TarefaResponseDTO> execute(Long idUsuario, Pageable pageable) {
        return tarefaRepository.findByIdUsuario(idUsuario, pageable)
                .map(mapper::toResponse);
    }
}
