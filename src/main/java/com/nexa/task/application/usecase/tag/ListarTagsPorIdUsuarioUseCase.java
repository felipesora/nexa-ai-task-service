package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTagsPorIdUsuarioUseCase {

    private final TagRepository tagRepository;
    private final TagControllerMapper mapper;

    public ListarTagsPorIdUsuarioUseCase(TagRepository tagRepository, TagControllerMapper mapper) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
    }

    public Page<TagResponseDTO> execute(Long idUsuario, Pageable pageable) {
        return tagRepository.findByIdUsuario(idUsuario, pageable)
                .map(mapper::toResponse);
    }
}
