package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceRequestDTO;
import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import com.nexa.task.infra.security.ForbiddenException;
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
class BuscarWorkspacePorIdUseCaseTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WorkspaceControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private BuscarWorkspacePorIdUseCase useCase;

    @Test
    void deveBuscarWorkspacePorIdComSucesso() {

        Long id = 1L;

        Workspace workspace = new WorkspaceBuilder()
                .comId(id)
                .comIdUsuario(1L)
                .comNome("Meu Workspace")
                .comDescricao("Descrição")
                .comAtivo(true)
                .build();

        WorkspaceResponseDTO response = new WorkspaceResponseDTO(
                id,
                1L,
                "Meu Workspace",
                "Descrição",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null,
                null
        );

        doNothing().when(authService).validateOwnerOrAdmin(workspace.getIdUsuario());

        when(workspaceRepository.findByIdAtivo(id))
                .thenReturn(Optional.of(workspace));

        when(mapper.toResponse(workspace))
                .thenReturn(response);

        WorkspaceResponseDTO resultado = useCase.execute(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("Meu Workspace", resultado.nome());
        assertEquals("Descrição", resultado.descricao());

        verify(authService).validateOwnerOrAdmin(workspace.getIdUsuario());
        verify(workspaceRepository).findByIdAtivo(id);
        verify(mapper).toResponse(workspace);
    }

    @Test
    void deveLancarExcecaoQuandoWorkspaceNaoForEncontrado() {

        Long id = 999L;

        when(workspaceRepository.findByIdAtivo(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(id)
        );

        assertEquals(
                "Workspace com id: 999 não encontrado.",
                exception.getMessage()
        );

        verify(workspaceRepository).findByIdAtivo(id);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoTemPermissao() {

        Long id = 1L;

        Workspace workspace = new WorkspaceBuilder()
                .comId(id)
                .comIdUsuario(1L)
                .build();

        when(workspaceRepository.findByIdAtivo(id))
                .thenReturn(Optional.of(workspace));

        doThrow(new ForbiddenException("Acesso negado"))
                .when(authService)
                .validateOwnerOrAdmin(workspace.getIdUsuario());

        ForbiddenException exception = assertThrows(
                ForbiddenException.class,
                () -> useCase.execute(id)
        );

        assertEquals("Acesso negado", exception.getMessage());

        verify(workspaceRepository).findByIdAtivo(id);
        verify(authService).validateOwnerOrAdmin(workspace.getIdUsuario());
        verify(mapper, never()).toResponse(any());
    }
}