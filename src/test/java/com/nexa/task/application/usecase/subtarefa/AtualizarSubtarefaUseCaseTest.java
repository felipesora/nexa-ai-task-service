package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaUpdateDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.subtarefa.SubtarefaBuilder;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarSubtarefaUseCaseTest {

    @Mock
    private SubtarefaRepository repository;

    @InjectMocks
    private AtualizarSubtarefaUseCase useCase;

    @Test
    void deveAtualizarSubtarefaComSucesso() {

        SubtarefaUpdateDTO dto = new SubtarefaUpdateDTO(
                "Subtarefa atualizada"
        );

        Subtarefa subtarefa = new SubtarefaBuilder()
                .comId(1L)
                .comTitulo("Subtarefa antiga")
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(subtarefa));

        useCase.execute(1L, dto);

        assertEquals(
                "Subtarefa atualizada",
                subtarefa.getTitulo()
        );

        assertNotNull(subtarefa.getAtualizadoEm());

        verify(repository).findById(1L);
        verify(repository).save(subtarefa);
    }

    @Test
    void deveLancarExcecaoQuandoSubtarefaNaoEncontrada() {

        SubtarefaUpdateDTO dto = new SubtarefaUpdateDTO(
                "Subtarefa atualizada"
        );

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> useCase.execute(999L, dto)
                );

        assertEquals(
                "Subtarefa com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(repository).findById(999L);
        verify(repository, never()).save(any());
    }
}