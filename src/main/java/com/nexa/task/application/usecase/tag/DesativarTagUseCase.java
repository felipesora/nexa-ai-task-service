package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.infra.security.AuthenticationService;

public class DesativarTagUseCase {

    private final TagRepository tagRepository;
    private final AuthenticationService authService;

    public DesativarTagUseCase(TagRepository tagRepository, AuthenticationService authService) {
        this.tagRepository = tagRepository;
        this.authService = authService;
    }

    public void execute(Long idTag) {
        Tag tag = tagRepository.findById(idTag)
                .orElseThrow(() -> new EntityNotFoundException("Tag com id: " + idTag + " não encontrada."));

        authService.validateOwnerOrAdmin(tag.getIdUsuario());

        tag.desativar();
        tagRepository.save(tag);
    }
}
