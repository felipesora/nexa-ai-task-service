package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
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
class BuscarTarefaPorIdUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private BuscarTarefaPorIdUseCase useCase;

    @Test
    void deveBuscarTarefaPorIdComSucesso() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .comTitulo("Minha tarefa")
                .build();

        TarefaResponseDTO response = mock(TarefaResponseDTO.class);

        when(tarefaRepository.findByIdAtivo(1L))
                .thenReturn(Optional.of(tarefa));

        when(mapper.toResponse(tarefa))
                .thenReturn(response);

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        TarefaResponseDTO resultado = useCase.execute(1L);

        assertNotNull(resultado);

        verify(tarefaRepository).findByIdAtivo(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(mapper).toResponse(tarefa);
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
                "Tarefa com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(tarefaRepository).findByIdAtivo(999L);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verifyNoInteractions(mapper);
    }
}