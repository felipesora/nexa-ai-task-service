package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IniciarTarefaUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private IniciarTarefaUseCase useCase;

    @Test
    void deveIniciarTarefaComSucesso() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .comStatus(StatusTarefa.PENDENTE)
                .build();

        when(tarefaRepository.findByIdAtivo(1L))
                .thenReturn(Optional.of(tarefa));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        useCase.execute(1L);

        assertEquals(StatusTarefa.EM_ANDAMENTO, tarefa.getStatus());
        assertNull(tarefa.getDataConclusao());
        assertNotNull(tarefa.getAtualizadoEm());

        verify(tarefaRepository).findByIdAtivo(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {

        when(tarefaRepository.findByIdAtivo(999L))
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

        verify(tarefaRepository).findByIdAtivo(999L);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(tarefaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTarefaJaEstiverEmAndamento() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .comStatus(StatusTarefa.EM_ANDAMENTO)
                .build();

        when(tarefaRepository.findByIdAtivo(1L))
                .thenReturn(Optional.of(tarefa));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> useCase.execute(1L)
                );

        assertEquals(
                "A tarefa já está em andamento.",
                exception.getMessage()
        );

        verify(tarefaRepository).findByIdAtivo(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(tarefaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTarefaEstiverConcluida() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .comStatus(StatusTarefa.CONCLUIDA)
                .build();

        when(tarefaRepository.findByIdAtivo(1L))
                .thenReturn(Optional.of(tarefa));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> useCase.execute(1L)
                );

        assertEquals(
                "Tarefas concluídas devem ser reabertas antes de serem iniciadas.",
                exception.getMessage()
        );

        verify(tarefaRepository).findByIdAtivo(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(tarefaRepository, never()).save(any());
    }
}