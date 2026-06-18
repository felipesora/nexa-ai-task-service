package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTarefasPorIdWorkspaceUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private TarefaControllerMapper mapper;

    @InjectMocks
    private ListarTarefasPorIdWorkspaceUseCase useCase;

    @Mock
    private Workspace workspace;

    @Mock
    private Tarefa tarefa;

    @Mock
    private TarefaResponseDTO response;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void deveListarTarefasPorIdWorkspace() {

        Page<Tarefa> page =
                new PageImpl<>(List.of(tarefa));

        when(workspaceRepository.findById(1L))
                .thenReturn(Optional.of(workspace));

        when(tarefaRepository.findByIdWorkspace(1L, pageable))
                .thenReturn(page);

        when(mapper.toResponse(tarefa))
                .thenReturn(response);

        Page<TarefaResponseDTO> resultado =
                useCase.execute(1L, pageable);

        assertEquals(1, resultado.getTotalElements());

        verify(workspaceRepository)
                .findById(1L);

        verify(tarefaRepository)
                .findByIdWorkspace(1L, pageable);

        verify(mapper)
                .toResponse(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoWorkspaceNaoForEncontrado() {

        when(workspaceRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> useCase.execute(999L, pageable)
                );

        assertEquals(
                "Workspace com id: 999 não encontrado.",
                exception.getMessage()
        );

        verify(tarefaRepository, never())
                .findByIdWorkspace(anyLong(), any());
    }
}