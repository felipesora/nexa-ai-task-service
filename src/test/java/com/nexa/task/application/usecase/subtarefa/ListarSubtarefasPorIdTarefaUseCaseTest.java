package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarSubtarefasPorIdTarefaUseCaseTest {

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private SubtarefaControllerMapper mapper;

    @InjectMocks
    private ListarSubtarefasPorIdTarefaUseCase useCase;

    @Mock
    private Tarefa tarefa;

    @Mock
    private Subtarefa subtarefa;

    @Mock
    private SubtarefaResponseDTO response;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void deveListarSubtarefasPorIdTarefa() {

        Page<Subtarefa> page =
                new PageImpl<>(List.of(subtarefa));

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        when(subtarefaRepository.findByIdTarefa(1L, pageable))
                .thenReturn(page);

        when(mapper.toResponse(subtarefa))
                .thenReturn(response);

        Page<SubtarefaResponseDTO> resultado =
                useCase.execute(1L, pageable);

        assertEquals(1, resultado.getTotalElements());

        verify(tarefaRepository)
                .findById(1L);

        verify(subtarefaRepository)
                .findByIdTarefa(1L, pageable);

        verify(mapper)
                .toResponse(subtarefa);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoForEncontrada() {

        when(tarefaRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> useCase.execute(999L, pageable)
                );

        assertEquals(
                "Tarefa com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(subtarefaRepository, never())
                .findByIdTarefa(anyLong(), any());
    }
}