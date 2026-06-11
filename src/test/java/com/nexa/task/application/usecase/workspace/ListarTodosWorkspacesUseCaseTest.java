package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTodosWorkspacesUseCaseTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WorkspaceControllerMapper mapper;

    @InjectMocks
    private ListarTodosWorkspacesUseCase useCase;

    @Test
    void deveListarTodosOsWorkspacesComPaginacao() {

        Pageable pageable = PageRequest.of(0, 10);

        Workspace workspace1 = new WorkspaceBuilder()
                .comId(1L)
                .comNome("Workspace Financeiro")
                .build();

        Workspace workspace2 = new WorkspaceBuilder()
                .comId(2L)
                .comNome("Workspace Estudos")
                .build();

        Page<Workspace> pageEntity = new PageImpl<>(
                List.of(workspace1, workspace2),
                pageable,
                2
        );

        WorkspaceResponseDTO response1 = new WorkspaceResponseDTO(
                1L,
                1L,
                "Workspace Financeiro",
                "",
                null,
                null,
                true,
                null,
                null
        );

        WorkspaceResponseDTO response2 = new WorkspaceResponseDTO(
                2L,
                1L,
                "Workspace Estudos",
                "",
                null,
                null,
                true,
                null,
                null
        );

        when(workspaceRepository.findAll(pageable))
                .thenReturn(pageEntity);

        when(mapper.toResponse(workspace1))
                .thenReturn(response1);

        when(mapper.toResponse(workspace2))
                .thenReturn(response2);

        Page<WorkspaceResponseDTO> resultado =
                useCase.execute(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());

        assertEquals("Workspace Financeiro",
                resultado.getContent().get(0).nome());

        assertEquals("Workspace Estudos",
                resultado.getContent().get(1).nome());

        verify(workspaceRepository).findAll(pageable);
        verify(mapper).toResponse(workspace1);
        verify(mapper).toResponse(workspace2);
    }
}