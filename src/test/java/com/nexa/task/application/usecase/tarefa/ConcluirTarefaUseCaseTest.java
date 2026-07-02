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
class ConcluirTarefaUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private ConcluirTarefaUseCase useCase;

    @Test
    void deveConcluirTarefaComSucesso() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .build();

        tarefa.setStatus(StatusTarefa.PENDENTE);

        when(tarefaRepository.findByIdAtivo(1L))
                .thenReturn(Optional.of(tarefa));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        useCase.execute(1L);

        assertEquals(
                StatusTarefa.CONCLUIDA,
                tarefa.getStatus()
        );
        assertNotNull(tarefa.getDataConclusao());
        assertNotNull(tarefa.getAtualizadoEm());

        verify(tarefaRepository).findByIdAtivo(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaJaEstiverConcluida() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .build();

        tarefa.setStatus(StatusTarefa.CONCLUIDA);

        when(tarefaRepository.findByIdAtivo(1L))
                .thenReturn(Optional.of(tarefa));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        BadRequestException exception =
                assertThrows(
                        BadRequestException.class,
                        () -> useCase.execute(1L)
                );

        assertEquals(
                "A tarefa já está concluída.",
                exception.getMessage()
        );

        verify(tarefaRepository).findByIdAtivo(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(tarefaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoForEncontrada() {

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
}