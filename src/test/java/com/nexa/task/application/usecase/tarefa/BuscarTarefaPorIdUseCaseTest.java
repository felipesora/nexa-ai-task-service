package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarTarefaPorIdUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaControllerMapper mapper;

    @InjectMocks
    private BuscarTarefaPorIdUseCase useCase;

    @Test
    void deveBuscarTarefaPorIdComSucesso() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comTitulo("Minha tarefa")
                .build();

        TarefaResponseDTO response =
                mock(TarefaResponseDTO.class);

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        when(mapper.toResponse(tarefa))
                .thenReturn(response);

        TarefaResponseDTO resultado =
                useCase.execute(1L);

        assertNotNull(resultado);

        verify(tarefaRepository).findById(1L);
        verify(mapper).toResponse(tarefa);
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
                "Tarefa com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(tarefaRepository).findById(999L);
        verifyNoInteractions(mapper);
    }
}