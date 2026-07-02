package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagCreateDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.infra.security.AuthenticatedUser;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

public class CadastrarTagUseCase {

    private final TagRepository tagRepository;
    private final CorTagRepository corTagRepository;
    private final TagControllerMapper mapper;
    private final AuthenticationService authService;

    public CadastrarTagUseCase(TagRepository tagRepository, CorTagRepository corTagRepository, TagControllerMapper mapper, AuthenticationService authService) {
        this.tagRepository = tagRepository;
        this.corTagRepository = corTagRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    @Transactional
    public TagResponseDTO execute(TagCreateDTO request) {
        AuthenticatedUser user = authService.getAuthenticatedUser();

        validarNomeUnicoDeTag(request.nome(), user.id());

        CorTag corTag = null;

        if (request.idCor() != null) {
            corTag = corTagRepository.findByIdAtivo(request.idCor())
                    .orElseThrow(() -> new EntityNotFoundException("Cor com id: " + request.idCor() + " não encontrada"));
        }

        Tag tag = mapper.toDomain(request, corTag, user.id());

        Tag salvo = tagRepository.save(tag);
        return mapper.toResponse(salvo);
    }

    private void validarNomeUnicoDeTag(String nome, Long idUsuario) {
        if (tagRepository.existsByNomeAndIdUsuario(nome, idUsuario)) {
            throw new BadRequestException("Já existe uma tag com esse nome para este usuário");
        }
    }
}
