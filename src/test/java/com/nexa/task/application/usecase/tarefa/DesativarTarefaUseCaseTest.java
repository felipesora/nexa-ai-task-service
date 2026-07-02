package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.subtarefa.SubtarefaBuilder;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesativarTarefaUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private DesativarTarefaUseCase useCase;

    @Test
    void deveDesativarTarefaComSucesso() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .comTitulo("Tarefa Teste")
                .comAtivo(true)
                .build();

        Subtarefa subtarefa1 = new SubtarefaBuilder()
                .comId(1L)
                .comTitulo("Subtarefa 1")
                .comAtivo(true)
                .build();

        Subtarefa subtarefa2 = new SubtarefaBuilder()
                .comId(2L)
                .comTitulo("Subtarefa 2")
                .comAtivo(true)
                .build();

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        when(subtarefaRepository.findAllByTarefa(1L))
                .thenReturn(List.of(subtarefa1, subtarefa2));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        useCase.execute(1L);

        assertFalse(tarefa.getAtivo());
        assertFalse(subtarefa1.getAtivo());
        assertFalse(subtarefa2.getAtivo());

        verify(tarefaRepository).findById(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(subtarefaRepository).findAllByTarefa(1L);
        verify(subtarefaRepository).save(subtarefa1);
        verify(subtarefaRepository).save(subtarefa2);
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {

        when(tarefaRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> useCase.execute(999L)
                );

        assertEquals(
                "Tarefa com id: 999 não encontrada",
                exception.getMessage()
        );

        verify(tarefaRepository).findById(999L);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(subtarefaRepository, never()).findAllByTarefa(anyLong());
        verify(subtarefaRepository, never()).save(any());
        verify(tarefaRepository, never()).save(any());
    }
}
