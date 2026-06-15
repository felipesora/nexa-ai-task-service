package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.EntityNotFoundException;
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
class DesativarTarefaUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private DesativarTarefaUseCase useCase;

    @Test
    void deveDesativarTarefaComSucesso() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comTitulo("Tarefa Teste")
                .comAtivo(true)
                .build();

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        useCase.execute(1L);

        assertFalse(tarefa.getAtivo());

        verify(tarefaRepository).findById(1L);
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {

        when(tarefaRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> useCase.execute(999L));

        assertEquals(
                "Tarefa com id: 999 nÃ£o encontrada",
                exception.getMessage()
        );

        verify(tarefaRepository).findById(999L);
        verify(tarefaRepository, never()).save(any());
    }
}
