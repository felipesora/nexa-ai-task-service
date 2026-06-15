package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IniciarTarefaUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private IniciarTarefaUseCase useCase;

    @Test
    void deveIniciarTarefaComSucesso() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comStatus(StatusTarefa.PENDENTE)
                .build();

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        useCase.execute(1L);

        assertEquals(StatusTarefa.EM_ANDAMENTO, tarefa.getStatus());
        assertNull(tarefa.getDataConclusao());
        assertNotNull(tarefa.getAtualizadoEm());

        verify(tarefaRepository).findById(1L);
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
        verify(tarefaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTarefaJaEstiverEmAndamento() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comStatus(StatusTarefa.EM_ANDAMENTO)
                .build();

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> useCase.execute(1L)
                );

        assertEquals(
                "A tarefa já está em andamento.",
                exception.getMessage()
        );

        verify(tarefaRepository).findById(1L);
        verify(tarefaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTarefaEstiverConcluida() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comStatus(StatusTarefa.CONCLUIDA)
                .build();

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> useCase.execute(1L)
                );

        assertEquals(
                "Tarefas concluídas devem ser reabertas antes de serem iniciadas.",
                exception.getMessage()
        );

        verify(tarefaRepository).findById(1L);
        verify(tarefaRepository, never()).save(any());
    }
}