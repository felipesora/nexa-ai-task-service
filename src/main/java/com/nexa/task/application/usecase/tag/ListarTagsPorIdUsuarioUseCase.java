package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTagsPorIdUsuarioUseCase {

    private final TagRepository tagRepository;
    private final TagControllerMapper mapper;
    private final AuthenticationService authService;

    public ListarTagsPorIdUsuarioUseCase(TagRepository tagRepository, TagControllerMapper mapper, AuthenticationService authService) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public Page<TagResponseDTO> execute(Long idUsuario, Pageable pageable) {
        authService.validateOwnerOrAdmin(idUsuario);

        return tagRepository.findByIdUsuarioAndAtivo(idUsuario, pageable)
                .map(mapper::toResponse);
    }
}
