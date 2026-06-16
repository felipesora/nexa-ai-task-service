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
class ListarSubtarefasPorIdTarefaUseCaseTest {

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @Mock
    private SubtarefaControllerMapper mapper;

    @InjectMocks
    private ListarSubtarefasPorIdTarefaUseCase useCase;

    @Test
    void deveListarSubtarefasPorIdTarefa() {

        Pageable pageable = PageRequest.of(0, 10);

        Subtarefa subtarefa = mock(Subtarefa.class);
        SubtarefaResponseDTO response = mock(SubtarefaResponseDTO.class);

        Page<Subtarefa> page =
                new PageImpl<>(List.of(subtarefa));

        when(subtarefaRepository.findByIdTarefa(1L, pageable))
                .thenReturn(page);

        when(mapper.toResponse(subtarefa))
                .thenReturn(response);

        Page<SubtarefaResponseDTO> resultado =
                useCase.execute(1L, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());

        verify(subtarefaRepository)
                .findByIdTarefa(1L, pageable);

        verify(mapper).toResponse(subtarefa);
    }
}