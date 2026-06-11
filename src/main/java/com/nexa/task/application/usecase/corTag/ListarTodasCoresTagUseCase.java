package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.mapper.CorTagControllerMapper;
import com.nexa.task.domain.repository.CorTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodasCoresTagUseCase {

    private final CorTagRepository repository;
    private final CorTagControllerMapper mapper;

    public ListarTodasCoresTagUseCase(CorTagRepository repository, CorTagControllerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Page<CorTagResponseDTO> execute(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
