package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTarefasPorIdUsuarioUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private ListarTarefasPorIdUsuarioUseCase useCase;

    @Test
    void deveListarTarefasPorIdUsuario() {

        Pageable pageable = PageRequest.of(0, 10);

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(1L)
                .comTitulo("Minha tarefa")
                .build();

        TarefaResponseDTO response = mock(TarefaResponseDTO.class);

        Page<Tarefa> page = new PageImpl<>(List.of(tarefa));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        when(tarefaRepository.findByIdUsuario(1L, pageable))
                .thenReturn(page);

        when(mapper.toResponse(tarefa))
                .thenReturn(response);

        Page<TarefaResponseDTO> resultado =
                useCase.execute(1L, pageable);

        assertEquals(1, resultado.getTotalElements());

        verify(authService).validateOwnerOrAdmin(1L);
        verify(tarefaRepository).findByIdUsuario(1L, pageable);
        verify(mapper).toResponse(tarefa);
    }
}