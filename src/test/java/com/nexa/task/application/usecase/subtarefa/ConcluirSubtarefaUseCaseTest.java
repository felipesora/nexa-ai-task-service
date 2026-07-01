package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcluirSubtarefaUseCaseTest {

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private ConcluirSubtarefaUseCase useCase;

    @Mock
    private Subtarefa subtarefa;

    @BeforeEach
    void setUp() {
        lenient().when(subtarefaRepository.findById(1L))
                .thenReturn(Optional.of(subtarefa));

        lenient().when(subtarefa.getIdUsuario())
                .thenReturn(10L);
    }

    @Test
    void deveConcluirSubtarefaComSucesso() {

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        useCase.execute(1L);

        verify(authService).validateOwnerOrAdmin(10L);
        verify(subtarefa).marcarConcluida();
        verify(subtarefaRepository).save(subtarefa);
    }

    @Test
    void deveLancarExcecaoQuandoSubtarefaNaoForEncontrada() {

        when(subtarefaRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(999L)
        );

        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(subtarefaRepository, never())
                .save(any(Subtarefa.class));
    }
}