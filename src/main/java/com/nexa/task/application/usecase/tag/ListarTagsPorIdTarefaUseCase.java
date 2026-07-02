package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListarTagsPorIdTarefaUseCase {

    private final TagRepository tagRepository;
    private final TarefaRepository tarefaRepository;
    private final TagControllerMapper mapper;
    private final AuthenticationService authService;

    public ListarTagsPorIdTarefaUseCase(TagRepository tagRepository, TarefaRepository tarefaRepository, TagControllerMapper mapper, AuthenticationService authService) {
        this.tagRepository = tagRepository;
        this.tarefaRepository = tarefaRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public Page<TagResponseDTO> execute(Long idTarefa, Pageable pageable) {
        Tarefa tarefa = tarefaRepository.findByIdAtivo(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada."));

        authService.validateOwnerOrAdmin(tarefa.getIdUsuario());

        return tagRepository.findByIdTarefaAndAtivo(idTarefa, pageable)
                .map(mapper::toResponse);
    }
}
