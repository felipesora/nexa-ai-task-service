package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

import java.util.Objects;

public class RemoverTagDaTarefaUseCase {

    private final TagRepository tagRepository;
    private final TarefaRepository tarefaRepository;
    private final AuthenticationService authService;

    public RemoverTagDaTarefaUseCase(TagRepository tagRepository, TarefaRepository tarefaRepository, AuthenticationService authService) {
        this.tagRepository = tagRepository;
        this.tarefaRepository = tarefaRepository;
        this.authService = authService;
    }

    @Transactional
    public void execute(Long idTarefa, Long idTag) {
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada."));

        authService.validateOwnerOrAdmin(tarefa.getIdUsuario());

        Tag tag = tagRepository.findById(idTag)
                .orElseThrow(() -> new EntityNotFoundException("Tag com id: " + idTag + " não encontrada."));

        if (!Objects.equals(tarefa.getIdUsuario(), tag.getIdUsuario())) {
            throw new BadRequestException("Esta tag não pertence ao mesmo usuário da tarefa.");
        }

        boolean naoEstaVinculada = tarefa.getTags().stream()
                .noneMatch(t -> Objects.equals(t.getId(), idTag));

        if (naoEstaVinculada) {
            throw new BadRequestException("A tag não está vinculada à tarefa.");
        }

        tarefa.getTags().removeIf(t -> Objects.equals(t.getId(), idTag));

        tarefaRepository.save(tarefa);
    }
}
