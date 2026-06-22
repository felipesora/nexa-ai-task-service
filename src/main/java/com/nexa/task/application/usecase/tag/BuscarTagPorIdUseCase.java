package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.TagRepository;

public class BuscarTagPorIdUseCase {

    private final TagRepository tagRepository;
    private final TagControllerMapper mapper;

    public BuscarTagPorIdUseCase(TagRepository tagRepository, TagControllerMapper mapper) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
    }

    public TagResponseDTO execute(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag com id: " + id + " não encontrada."));

        return mapper.toResponse(tag);
    }
}
