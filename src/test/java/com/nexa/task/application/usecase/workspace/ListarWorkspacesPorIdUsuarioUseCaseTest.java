package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarWorkspacesPorIdUsuarioUseCaseTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WorkspaceControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private ListarWorkspacesPorIdUsuarioUseCase useCase;

    @Test
    void deveListarWorkspacesPorIdUsuarioComPaginacao() {

        Long idUsuario = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Workspace workspace1 = new WorkspaceBuilder()
                .comId(1L)
                .comIdUsuario(idUsuario)
                .comNome("Workspace Financeiro")
                .build();

        Workspace workspace2 = new WorkspaceBuilder()
                .comId(2L)
                .comIdUsuario(idUsuario)
                .comNome("Workspace Estudos")
                .build();

        Page<Workspace> pageEntity = new PageImpl<>(
                List.of(workspace1, workspace2),
                pageable,
                2
        );

        WorkspaceResponseDTO response1 = new WorkspaceResponseDTO(
                1L,
                idUsuario,
                "Workspace Financeiro",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null,
                null
        );

        WorkspaceResponseDTO response2 = new WorkspaceResponseDTO(
                2L,
                idUsuario,
                "Workspace Estudos",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null,
                null
        );

        doNothing().when(authService).validateOwnerOrAdmin(idUsuario);

        when(workspaceRepository.findByIdUsuarioAndAtivo(idUsuario, pageable))
                .thenReturn(pageEntity);

        when(mapper.toResponse(workspace1))
                .thenReturn(response1);

        when(mapper.toResponse(workspace2))
                .thenReturn(response2);

        Page<WorkspaceResponseDTO> resultado =
                useCase.execute(idUsuario, pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());

        assertEquals(
                "Workspace Financeiro",
                resultado.getContent().get(0).nome()
        );

        assertEquals(
                "Workspace Estudos",
                resultado.getContent().get(1).nome()
        );

        verify(authService).validateOwnerOrAdmin(idUsuario);
        verify(workspaceRepository).findByIdUsuarioAndAtivo(idUsuario, pageable);
        verify(mapper).toResponse(workspace1);
        verify(mapper).toResponse(workspace2);
    }
}