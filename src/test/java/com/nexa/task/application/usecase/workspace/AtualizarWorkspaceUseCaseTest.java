package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceRequestDTO;
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

    @InjectMocks
    private AtualizarWorkspaceUseCase useCase;

    @Test
    void deveAtualizarWorkspaceComSucesso() {

        Long idWorkspace = 1L;

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                1L,
                "Workspace Atualizado",
                "Nova descrição",
                1L,
                1L
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(idWorkspace)
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

        when(workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(
                request.nome(),
                request.idUsuario(),
                idWorkspace))
                .thenReturn(false);

        when(corWorkspaceRepository.findById(1L))
                .thenReturn(Optional.of(cor));

        when(iconeWorkspaceRepository.findById(1L))
                .thenReturn(Optional.of(icone));

        useCase.execute(idWorkspace, request);

        verify(workspaceRepository).save(workspace);

        assertEquals("Workspace Atualizado", workspace.getNome());
        assertEquals("Nova descrição", workspace.getDescricao());
        assertEquals(1L, workspace.getIdUsuario());
        assertEquals(cor, workspace.getCorWorkspace());
        assertEquals(icone, workspace.getIconeWorkspace());

        assertNotNull(workspace.getAtualizadoEm());
    }

    @Test
    void deveLancarExcecaoQuandoWorkspaceNaoForEncontrado() {

        Long idWorkspace = 999L;

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                1L,
                "Workspace",
                "Descrição",
                1L,
                1L
        );

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(idWorkspace, request)
        );

        assertEquals(
                "Workspace com id: 999 não encontrado",
                exception.getMessage()
        );

        verify(workspaceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExistir() {

        Long idWorkspace = 1L;

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                1L,
                "Workspace Existente",
                "Descrição",
                1L,
                1L
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(idWorkspace)
                .build();

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.of(workspace));

        when(workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(
                request.nome(),
                request.idUsuario(),
                idWorkspace))
                .thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(idWorkspace, request)
        );

        assertEquals(
                "Já existe um workspace com esse nome para este usuário",
                exception.getMessage()
        );

        verify(workspaceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCorNaoForEncontrada() {

        Long idWorkspace = 1L;

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                1L,
                "Workspace",
                "Descrição",
                99L,
                1L
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(idWorkspace)
                .build();

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.of(workspace));

        when(workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(
                request.nome(),
                request.idUsuario(),
                idWorkspace))
                .thenReturn(false);

        when(corWorkspaceRepository.findById(99L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(idWorkspace, request)
        );

        assertEquals(
                "Cor com id: 99 não encontrada",
                exception.getMessage()
        );

        verify(workspaceRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoIconeNaoForEncontrado() {

        Long idWorkspace = 1L;

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                1L,
                "Workspace",
                "Descrição",
                1L,
                99L
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(idWorkspace)
                .build();

        CorWorkspace cor = new CorWorkspaceBuilder()
                .comId(1L)
                .build();

        when(workspaceRepository.findById(idWorkspace))
                .thenReturn(Optional.of(workspace));

        when(workspaceRepository.existsByNomeAndIdUsuarioAndIdNot(
                request.nome(),
                request.idUsuario(),
                idWorkspace))
                .thenReturn(false);

        when(corWorkspaceRepository.findById(1L))
                .thenReturn(Optional.of(cor));

        when(iconeWorkspaceRepository.findById(99L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(idWorkspace, request)
        );

        assertEquals(
                "Ícone com id: 99 não encontrado",
                exception.getMessage()
        );

        verify(workspaceRepository, never()).save(any());
    }
}