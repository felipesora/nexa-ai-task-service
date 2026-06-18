package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaCreateDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.builder.subtarefa.SubtarefaBuilder;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
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
class CadastrarSubtarefaUseCaseTest {

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private SubtarefaControllerMapper mapper;

    @InjectMocks
    private CadastrarSubtarefaUseCase useCase;

    @Test
    void deveCadastrarSubtarefaComSucesso() {

        SubtarefaCreateDTO request = new SubtarefaCreateDTO(
                "Minha subtarefa",
                1L
        );

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comTitulo("Minha tarefa")
                .build();

        Subtarefa subtarefa = new SubtarefaBuilder()
                .comId(1L)
                .comTitulo("Minha subtarefa")
                .build();

        SubtarefaResponseDTO response = new SubtarefaResponseDTO(
                1L,
                "Minha subtarefa",
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                1L
        );

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        when(mapper.toDomain(request, tarefa))
                .thenReturn(subtarefa);

        when(subtarefaRepository.save(subtarefa))
                .thenReturn(subtarefa);

        when(mapper.toResponse(subtarefa))
                .thenReturn(response);

        SubtarefaResponseDTO resultado = useCase.execute(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Minha subtarefa", resultado.titulo());
        assertFalse(resultado.concluida());
        assertTrue(resultado.ativo());
        assertEquals(1L, resultado.idTarefa());

        verify(tarefaRepository).findById(1L);
        verify(mapper).toDomain(request, tarefa);
        verify(subtarefaRepository).save(subtarefa);
        verify(mapper).toResponse(subtarefa);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {

        SubtarefaCreateDTO request = new SubtarefaCreateDTO(
                "Minha subtarefa",
                999L
        );

        when(tarefaRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> useCase.execute(request)
                );

        assertEquals(
                "Tarefa com id: 999 não encontrada",
                exception.getMessage()
        );

        verify(tarefaRepository).findById(999L);
        verifyNoInteractions(subtarefaRepository);
        verifyNoInteractions(mapper);
    }
}