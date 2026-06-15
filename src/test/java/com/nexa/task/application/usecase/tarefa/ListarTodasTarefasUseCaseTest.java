package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTodasTarefasUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaControllerMapper mapper;

    @InjectMocks
    private ListarTodasTarefasUseCase useCase;

    @Test
    void deveListarTodasAsTarefas() {

        Pageable pageable = PageRequest.of(0, 10);

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comTitulo("Minha tarefa")
                .build();

        TarefaResponseDTO response = mock(TarefaResponseDTO.class);

        Page<Tarefa> page =
                new PageImpl<>(List.of(tarefa));

        when(tarefaRepository.findAll(pageable))
                .thenReturn(page);

        when(mapper.toResponse(tarefa))
                .thenReturn(response);

        Page<TarefaResponseDTO> resultado =
                useCase.execute(pageable);

        assertEquals(1, resultado.getTotalElements());

        verify(tarefaRepository).findAll(pageable);
        verify(mapper).toResponse(tarefa);
    }
}