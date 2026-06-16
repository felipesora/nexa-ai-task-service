package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.repository.SubtarefaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodasSubtarefasUseCase {

    private final SubtarefaRepository subtarefaRepository;
    private final SubtarefaControllerMapper mapper;

    public ListarTodasSubtarefasUseCase(SubtarefaRepository subtarefaRepository, SubtarefaControllerMapper mapper) {
        this.subtarefaRepository = subtarefaRepository;
        this.mapper = mapper;
    }

    public Page<SubtarefaResponseDTO> execute(Pageable pageable) {
        return subtarefaRepository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
