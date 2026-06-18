package com.nexa.task.application.usecase.subtarefa;

import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.mapper.SubtarefaControllerMapper;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTodasSubtarefasUseCaseTest {

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @Mock
    private SubtarefaControllerMapper mapper;

    @InjectMocks
    private ListarTodasSubtarefasUseCase useCase;

    @Test
    void deveListarTodasSubtarefas() {

        Pageable pageable = PageRequest.of(0, 10);

        Subtarefa subtarefa = mock(Subtarefa.class);
        SubtarefaResponseDTO response = mock(SubtarefaResponseDTO.class);

        Page<Subtarefa> page =
                new PageImpl<>(List.of(subtarefa));

        when(subtarefaRepository.findAll(pageable))
                .thenReturn(page);

        when(mapper.toResponse(subtarefa))
                .thenReturn(response);

        Page<SubtarefaResponseDTO> resultado =
                useCase.execute(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());

        verify(subtarefaRepository).findAll(pageable);
        verify(mapper).toResponse(subtarefa);
    }
}