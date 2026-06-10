package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.CorTagControllerMapper;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.repository.CorTagRepository;

public class BuscarCorTagPorIdUserCase {

    private final CorTagRepository repository;
    private final CorTagControllerMapper mapper;

    public BuscarCorTagPorIdUserCase(CorTagRepository repository, CorTagControllerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CorTagResponseDTO execute(Long id) {
        CorTag cor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cor da Tag com id: " + id + " não encontrada."));

        return mapper.toResponse(cor);
    }
}
