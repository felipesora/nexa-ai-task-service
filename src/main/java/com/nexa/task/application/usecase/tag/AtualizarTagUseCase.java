package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagUpdateDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

public class AtualizarTagUseCase {

    private final TagRepository tagRepository;
    private final CorTagRepository corTagRepository;
    private final AuthenticationService authService;

    public AtualizarTagUseCase(TagRepository tagRepository, CorTagRepository corTagRepository, AuthenticationService authService) {
        this.tagRepository = tagRepository;
        this.corTagRepository = corTagRepository;
        this.authService = authService;
    }

    @Transactional
    public void execute(Long idTag, TagUpdateDTO updateDTO) {
        Tag tag = tagRepository.findByIdAtivo(idTag)
                .orElseThrow(() -> new EntityNotFoundException("Tag com id: " + idTag + " não encontrada."));

        authService.validateOwnerOrAdmin(tag.getIdUsuario());

        validarNomeUnicoDeTag(tag, updateDTO.nome());

        CorTag corTag = null;

        if (updateDTO.idCor() != null) {
            corTag = corTagRepository.findByIdAtivo(updateDTO.idCor())
                    .orElseThrow(() -> new EntityNotFoundException("Cor com id: " + updateDTO.idCor() + " não encontrada"));
        }

        tag.setNome(updateDTO.nome());
        tag.setCorTag(corTag);
        tagRepository.save(tag);
    }

    private void validarNomeUnicoDeTag(Tag tag, String nome) {
        if (tagRepository.existsByNomeAndIdUsuarioAndIdNot(nome, tag.getIdUsuario(), tag.getId())) {
            throw new BadRequestException("Já existe uma tag com esse nome para este usuário");
        }
    }
}
