package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaCreateDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;

import java.util.LinkedHashSet;
import java.util.Objects;

public class CadastrarTarefaUseCase {

    private final TarefaRepository tarefaRepository;
    private final WorkspaceRepository workspaceRepository;
    private final TagRepository tagRepository;
    private final TarefaControllerMapper mapper;
    private final AuthenticationService authService;

    public CadastrarTarefaUseCase(TarefaRepository tarefaRepository, WorkspaceRepository workspaceRepository, TagRepository tagRepository, TarefaControllerMapper mapper, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.workspaceRepository = workspaceRepository;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.authService = authService;
    }

    public TarefaResponseDTO execute(TarefaCreateDTO dto) {
        Workspace workspace = workspaceRepository.findByIdAtivo(dto.idWorkspace())
                .orElseThrow(() -> new EntityNotFoundException("Workspace com id: " + dto.idWorkspace() + " não encontrado"));

        authService.validateOwnerOrAdmin(workspace.getIdUsuario());

        Tarefa tarefa = mapper.toDomain(dto, workspace, workspace.getIdUsuario());

        // Mesmas regras do vínculo avulso: tag ativa e do mesmo usuário da tarefa (ids repetidos são ignorados)
        if (dto.idTags() != null) {
            for (Long idTag : new LinkedHashSet<>(dto.idTags())) {
                Tag tag = tagRepository.findByIdAtivo(idTag)
                        .orElseThrow(() -> new EntityNotFoundException("Tag com id: " + idTag + " não encontrada."));

                if (!Objects.equals(tarefa.getIdUsuario(), tag.getIdUsuario())) {
                    throw new BadRequestException("Esta tag não pertence ao mesmo usuário da tarefa.");
                }

                tarefa.adicionarTag(tag);
            }
        }

        Tarefa salvo = tarefaRepository.save(tarefa);
        return mapper.toResponse(salvo);
    }
}
