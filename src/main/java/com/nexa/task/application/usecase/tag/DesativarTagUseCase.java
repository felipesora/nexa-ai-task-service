package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.TagRepository;

public class DesativarTagUseCase {

    private final TagRepository tagRepository;

    public DesativarTagUseCase(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public void execute(Long idTag) {
        Tag tag = tagRepository.findById(idTag)
                .orElseThrow(() -> new EntityNotFoundException("Tag com id: " + idTag + " não encontrada."));

        tag.desativar();
        tagRepository.save(tag);
    }
}
