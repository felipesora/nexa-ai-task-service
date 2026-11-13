package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkspaceControllerMapperTest {

    @Mock
    private TarefaRepository tarefaRepository;

    private WorkspaceControllerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new WorkspaceControllerMapper(
                new CorWorkspaceControllerMapper(),
                new IconeWorkspaceControllerMapper(),
                tarefaRepository
        );
    }

    @Test
    void deveIncluirQuantidadeDeTarefasAtivasNoResponse() {

        Workspace workspace = new WorkspaceBuilder()
                .comId(1L)
                .comNome("Meu Workspace")
                .build();

        when(tarefaRepository.countAtivasByIdWorkspace(1L)).thenReturn(5L);

        WorkspaceResponseDTO response = mapper.toResponse(workspace);

        assertEquals(1L, response.id());
        assertEquals("Meu Workspace", response.nome());
        assertEquals(5L, response.quantidadeTarefas());
        verify(tarefaRepository).countAtivasByIdWorkspace(1L);
    }

    @Test
    void deveRetornarZeroQuandoWorkspaceNaoTemTarefas() {

        Workspace workspace = new WorkspaceBuilder()
                .comId(2L)
                .build();

        when(tarefaRepository.countAtivasByIdWorkspace(2L)).thenReturn(0L);

        WorkspaceResponseDTO response = mapper.toResponse(workspace);

        assertEquals(0L, response.quantidadeTarefas());
    }
}
