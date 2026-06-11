package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.repository.CorTagRepository;

public class AtivarCorTagUseCase {

    private final CorTagRepository repository;

    public AtivarCorTagUseCase(CorTagRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        CorTag cor = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cor da Tag com id: " + id + " não encontrada."));

        cor.ativar();
        repository.save(cor);
    }
}
