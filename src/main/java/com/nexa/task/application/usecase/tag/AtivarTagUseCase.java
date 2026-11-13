package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.TagRepository;

public class AtivarTagUseCase {

    private final TagRepository tagRepository;

    public AtivarTagUseCase(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public void execute(Long idTag) {
        Tag tag = tagRepository.findById(idTag)
                .orElseThrow(() -> new EntityNotFoundException("Tag com id: " + idTag + " não encontrada."));

        if (tagRepository.existsAtivoByNomeAndIdUsuarioAndIdNot(tag.getNome(), tag.getIdUsuario(), tag.getId())) {
            throw new BadRequestException("Já existe uma tag ativa com esse nome para este usuário");
        }

        tag.ativar();
        tagRepository.save(tag);
    }
}
