package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.TagRepository;
import jakarta.transaction.Transactional;

public class DesativarCorTagUseCase {

    private final CorTagRepository repository;
    private final TagRepository tagRepository;

    public DesativarCorTagUseCase(CorTagRepository repository, TagRepository tagRepository) {
        this.repository = repository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public void execute(Long id) {
        CorTag cor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cor da Tag com id: " + id + " não encontrada."));

        tagRepository.removerCorDasTags(id);

        cor.desativar();
        repository.save(cor);
    }
}
