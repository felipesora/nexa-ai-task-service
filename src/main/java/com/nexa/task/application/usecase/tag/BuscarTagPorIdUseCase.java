package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.infra.security.AuthenticationService;

public class BuscarTagPorIdUseCase {

    private final TagRepository tagRepository;
    private final TagControllerMapper mapper;
    private final AuthenticationService authService;

    public BuscarTagPorIdUseCase(TagRepository tagRepository, TagControllerMapper mapper, AuthenticationService authService) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public TagResponseDTO execute(Long id) {
        Tag tag = tagRepository.findByIdAtivo(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag com id: " + id + " não encontrada."));

        authService.validateOwnerOrAdmin(tag.getIdUsuario());

        return mapper.toResponse(tag);
    }
}
