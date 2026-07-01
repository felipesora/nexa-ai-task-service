package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
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
class BuscarSubtarefaPorIdUseCaseTest {

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @Mock
    private SubtarefaControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private BuscarSubtarefaPorIdUseCase useCase;

    @Test
    void deveBuscarSubtarefaPorIdComSucesso() {

        Subtarefa subtarefa = mock(Subtarefa.class);
        SubtarefaResponseDTO response = mock(SubtarefaResponseDTO.class);

        when(subtarefaRepository.findById(1L))
                .thenReturn(Optional.of(subtarefa));

        when(subtarefa.getIdUsuario())
                .thenReturn(10L);

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        when(mapper.toResponse(subtarefa))
                .thenReturn(response);

        SubtarefaResponseDTO resultado = useCase.execute(1L);

        assertNotNull(resultado);

        verify(subtarefaRepository).findById(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(mapper).toResponse(subtarefa);
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
                "Subtarefa com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(subtarefaRepository).findById(999L);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verifyNoInteractions(mapper);
    }
}