package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.subtarefa.SubtarefaBuilder;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesativarSubtarefaUseCaseTest {

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private DesativarSubtarefaUseCase useCase;

    @Test
    void deveDesativarSubtarefaComSucesso() {

        Subtarefa subtarefa = new SubtarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .comTitulo("Subtarefa teste")
                .comAtivo(true)
                .build();

        when(subtarefaRepository.findById(1L))
                .thenReturn(Optional.of(subtarefa));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        useCase.execute(1L);

        assertFalse(subtarefa.getAtivo());

        verify(subtarefaRepository).findById(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(subtarefaRepository).save(subtarefa);
    }

    @Test
    void deveLancarExcecaoQuandoSubtarefaNaoEncontrada() {

        when(subtarefaRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> useCase.execute(999L)
                );

        assertEquals(
                "Subtarefa com id: 999 não encontrada",
                exception.getMessage()
        );

        verify(subtarefaRepository).findById(999L);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(subtarefaRepository, never()).save(any());
    }
}
