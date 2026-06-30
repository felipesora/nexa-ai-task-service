package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaUpdateDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public class AtualizarTarefaUseCase {

    private final TarefaRepository tarefaRepository;
    private final AuthenticationService authService;

    public AtualizarTarefaUseCase(TarefaRepository tarefaRepository, AuthenticationService authService) {
        this.tarefaRepository = tarefaRepository;
        this.authService = authService;
    }

    @Transactional
    public void execute(Long idTarefa, TarefaUpdateDTO dto) {
        Tarefa tarefa = tarefaRepository.findById(idTarefa)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa com id: " + idTarefa + " não encontrada"));

        authService.validateOwnerOrAdmin(tarefa.getIdUsuario());

        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setPrioridade(dto.prioridade());
        tarefa.setDificuldade(dto.dificuldade());
        tarefa.setDataLimite(dto.dataLimite());
        tarefa.setAtualizadoEm(LocalDateTime.now());

        tarefaRepository.save(tarefa);
    }
}
