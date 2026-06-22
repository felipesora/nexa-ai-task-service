package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagCreateDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.TagRepository;
import jakarta.transaction.Transactional;

public class CadastrarTagUseCase {

    private final TagRepository tagRepository;
    private final CorTagRepository corTagRepository;
    private final TagControllerMapper mapper;

    public CadastrarTagUseCase(TagRepository tagRepository, CorTagRepository corTagRepository, TagControllerMapper mapper) {
        this.tagRepository = tagRepository;
        this.corTagRepository = corTagRepository;
        this.mapper = mapper;
    }

    @Transactional
    public TagResponseDTO execute(TagCreateDTO request) {
        CorTag corTag = null;

        if (request.idCor() != null) {
            corTag = corTagRepository.findById(request.idCor())
                    .orElseThrow(() -> new EntityNotFoundException("Cor com id: " + request.idCor() + " não encontrada"));
        }

        Tag salvo = tagRepository.save(mapper.toDomain(request, corTag));
        return mapper.toResponse(salvo);
    }
}
