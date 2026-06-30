package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceUpdateDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.workspace.CorWorkspaceBuilder;
import com.nexa.task.domain.builder.workspace.IconeWorkspaceBuilder;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import com.nexa.task.infra.security.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarWorkspaceUseCaseTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private CorWorkspaceRepository corWorkspaceRepository;

    @Mock
    private IconeWorkspaceRepository iconeWorkspaceRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private AtualizarWorkspaceUseCase useCase;

    @Test
    void deveAtualizarWorkspaceComSucesso() {

        Long idWorkspace = 1L;

        WorkspaceUpdateDTO updateDTO = new WorkspaceUpdateDTO(
                "Workspace Atualizado",
                "Nova descrição",
                1L,
                1L
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(idWorkspace)
                .comIdUsuario(1L)
                .comNome("Workspace Antigo")
                .build();

        CorWorkspace cor = new CorWorkspaceBuilder()
                .comId(1L)
                .build();

        IconeWorkspace icone = new IconeWorkspaceBuilder()
                .comId(1L)
                .build();

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.of(workspace));

        doNothing().when(authService).validateOwnerOrAdmin(1L);

        when(workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(
                updateDTO.nome(),
                workspace.getIdUsuario(),
                workspace.getId()))
                .thenReturn(false);

        when(corWorkspaceRepository.findById(1L))
                .thenReturn(Optional.of(cor));

        when(iconeWorkspaceRepository.findById(1L))
                .thenReturn(Optional.of(icone));

        useCase.execute(idWorkspace, updateDTO);

        assertEquals("Workspace Atualizado", workspace.getNome());
        assertEquals("Nova descrição", workspace.getDescricao());
        assertEquals(cor, workspace.getCorWorkspace());
        assertEquals(icone, workspace.getIconeWorkspace());
        assertNotNull(workspace.getAtualizadoEm());

        verify(authService).validateOwnerOrAdmin(1L);
        verify(workspaceRepository).save(workspace);
    }

    @Test
    void deveLancarExcecaoQuandoWorkspaceNaoForEncontrado() {

        Long idWorkspace = 999L;

        WorkspaceUpdateDTO updateDTO = new WorkspaceUpdateDTO(
                "Workspace",
                "Descrição",
                1L,
                1L
        );

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(idWorkspace, updateDTO)
        );

        assertEquals(
                "Workspace com id: 999 não encontrado",
                exception.getMessage()
        );

        verify(workspaceRepository, never()).save(any());
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExistir() {

        Long idWorkspace = 1L;

        WorkspaceUpdateDTO updateDTO = new WorkspaceUpdateDTO(
                "Workspace Existente",
                "Descrição",
                1L,
                1L
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(idWorkspace)
                .comIdUsuario(1L)
                .build();

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.of(workspace));

        when(workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(
                updateDTO.nome(),
                workspace.getIdUsuario(),
                workspace.getId()))
                .thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(idWorkspace, updateDTO)
        );

        assertEquals(
                "Já existe um workspace com esse nome para este usuário",
                exception.getMessage()
        );

        verify(authService).validateOwnerOrAdmin(1L);
        verify(workspaceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCorNaoForEncontrada() {

        Long idWorkspace = 1L;

        WorkspaceUpdateDTO updateDTO = new WorkspaceUpdateDTO(
                "Workspace",
                "Descrição",
                99L,
                1L
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(idWorkspace)
                .comIdUsuario(1L)
                .build();

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.of(workspace));

        when(workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(
                updateDTO.nome(),
                workspace.getIdUsuario(),
                workspace.getId()))
                .thenReturn(false);

        when(corWorkspaceRepository.findById(99L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(idWorkspace, updateDTO)
        );

        assertEquals(
                "Cor com id: 99 não encontrada",
                exception.getMessage()
        );

        verify(authService).validateOwnerOrAdmin(1L);
        verify(workspaceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoIconeNaoForEncontrado() {

        Long idWorkspace = 1L;

        WorkspaceUpdateDTO updateDTO = new WorkspaceUpdateDTO(
                "Workspace",
                "Descrição",
                1L,
                99L
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(idWorkspace)
                .comIdUsuario(1L)
                .build();

        CorWorkspace cor = new CorWorkspaceBuilder()
                .comId(1L)
                .build();

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.of(workspace));

        when(workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(
                updateDTO.nome(),
                workspace.getIdUsuario(),
                workspace.getId()))
                .thenReturn(false);

        when(corWorkspaceRepository.findById(1L))
                .thenReturn(Optional.of(cor));

        when(iconeWorkspaceRepository.findById(99L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(idWorkspace, updateDTO)
        );

        assertEquals(
                "Ícone com id: 99 não encontrado",
                exception.getMessage()
        );

        verify(authService).validateOwnerOrAdmin(1L);
        verify(workspaceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoTemPermissao() {

        Long idWorkspace = 1L;

        WorkspaceUpdateDTO updateDTO = new WorkspaceUpdateDTO(
                "Workspace",
                "Descrição",
                null,
                null
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(idWorkspace)
                .comIdUsuario(1L)
                .build();

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.of(workspace));

        doThrow(new ForbiddenException("Acesso negado"))
                .when(authService)
                .validateOwnerOrAdmin(1L);

        assertThrows(
                ForbiddenException.class,
                () -> useCase.execute(idWorkspace, updateDTO)
        );

        verify(workspaceRepository).findById(idWorkspace);
        verify(authService).validateOwnerOrAdmin(1L);

        verify(workspaceRepository, never()).existsByNomeAndIdUsuarioAndIdNot(any(), any(), any());
        verify(workspaceRepository, never()).save(any());
        verifyNoInteractions(corWorkspaceRepository);
        verifyNoInteractions(iconeWorkspaceRepository);
    }
}