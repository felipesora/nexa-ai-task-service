package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTodasTagsUseCase {

    private final TagRepository tagRepository;
    private final TagControllerMapper mapper;

    public ListarTodasTagsUseCase(TagRepository tagRepository, TagControllerMapper mapper) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
    }

    public Page<TagResponseDTO> execute(Pageable pageable) {
        return tagRepository.findAll(pageable)
                .map(mapper::toResponse);
    }
}
