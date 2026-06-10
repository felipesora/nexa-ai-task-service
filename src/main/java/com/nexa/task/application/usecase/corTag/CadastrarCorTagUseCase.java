package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.dto.corTag.CorTagRequestDTO;
import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.mapper.CorTagControllerMapper;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.repository.CorTagRepository;

import java.util.Optional;

public class CadastrarCorTagUseCase {

    private final CorTagRepository repository;
    private final CorTagControllerMapper mapper;

    public CadastrarCorTagUseCase(CorTagRepository repository, CorTagControllerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public CorTagResponseDTO execute(CorTagRequestDTO request) {
        Optional<CorTag> corExiste = repository.findByCor(request.cor());

        if (corExiste.isPresent()) {
            throw new BadRequestException("Esta cor de tag já está cadastrada.");
        }

        CorTag salvo = repository.save(mapper.toDomain(request));
        return mapper.toResponse(salvo);
    }
}
