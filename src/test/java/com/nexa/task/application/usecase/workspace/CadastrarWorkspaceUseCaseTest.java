package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceRequestDTO;
import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.builder.workspace.CorWorkspaceBuilder;
import com.nexa.task.domain.builder.workspace.IconeWorkspaceBuilder;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticatedUser;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarWorkspaceUseCaseTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private CorWorkspaceRepository corWorkspaceRepository;

    @Mock
    private IconeWorkspaceRepository iconeWorkspaceRepository;

    @Mock
    private WorkspaceControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private CadastrarWorkspaceUseCase useCase;

    private final AuthenticatedUser user =
            new AuthenticatedUser(
                    1L,
                    "admin@email.com",
                    "ADMIN"
            );

    @Test
    void deveCadastrarWorkspaceComSucessoSemCorEIcone() {

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                "Nome Workspace",
                "Descrição Workspace",
                null,
                null
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(1L)
                .comIdUsuario(1L)
                .comNome("Nome Workspace")
                .comDescricao("Descrição Workspace")
                .comAtivo(true)
                .comCor(null)
                .comIcone(null)
                .build();

        WorkspaceResponseDTO response = new WorkspaceResponseDTO(
                1L,
                1L,
                "Nome Workspace",
                "Descrição Workspace",
                workspace.getCriadoEm(),
                workspace.getAtualizadoEm(),
                true,
                null,
                null
        );

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(workspaceRepository.existsByNomeAndIdUsuario(request.nome(), 1L)).thenReturn(false);
        when(mapper.toDomain(request, null, null, 1L)).thenReturn(workspace);
        when(workspaceRepository.save(workspace)).thenReturn(workspace);
        when(mapper.toResponse(workspace)).thenReturn(response);

        WorkspaceResponseDTO resultado = useCase.execute(request);

        assertNotNull(resultado);
        assertEquals(response, resultado);

        verify(authService).getAuthenticatedUser();
        verify(workspaceRepository).existsByNomeAndIdUsuario(request.nome(), 1L);
        verify(mapper).toDomain(request, null, null, 1L);
        verify(workspaceRepository).save(workspace);
        verify(mapper).toResponse(workspace);
    }

    @Test
    void deveCadastrarWorkspaceComSucessoComCorEIcone() {

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                "Workspace",
                "Descrição",
                1L,
                1L
        );

        CorWorkspace cor = new CorWorkspaceBuilder()
                .comId(1L)
                .build();

        IconeWorkspace icone = new IconeWorkspaceBuilder()
                .comId(1L)
                .build();

        Workspace workspace = new WorkspaceBuilder()
                .comId(1L)
                .comIdUsuario(1L)
                .comNome("Workspace")
                .comDescricao("Descrição")
                .comCor(cor)
                .comIcone(icone)
                .build();

        WorkspaceResponseDTO response = new WorkspaceResponseDTO(
                1L,
                1L,
                "Workspace",
                "Descrição",
                workspace.getCriadoEm(),
                workspace.getAtualizadoEm(),
                true,
                null,
                null
        );

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(workspaceRepository.existsByNomeAndIdUsuario(request.nome(), 1L)).thenReturn(false);
        when(corWorkspaceRepository.findByIdAtivo(1L)).thenReturn(Optional.of(cor));
        when(iconeWorkspaceRepository.findByIdAtivo(1L)).thenReturn(Optional.of(icone));
        when(mapper.toDomain(request, cor, icone, 1L)).thenReturn(workspace);
        when(workspaceRepository.save(workspace)).thenReturn(workspace);
        when(mapper.toResponse(workspace)).thenReturn(response);

        WorkspaceResponseDTO resultado = useCase.execute(request);

        assertNotNull(resultado);
        assertEquals(response, resultado);

        verify(authService).getAuthenticatedUser();
        verify(workspaceRepository).existsByNomeAndIdUsuario(request.nome(), 1L);
        verify(corWorkspaceRepository).findByIdAtivo(1L);
        verify(iconeWorkspaceRepository).findByIdAtivo(1L);
        verify(mapper).toDomain(request, cor, icone, 1L);
        verify(workspaceRepository).save(workspace);
        verify(mapper).toResponse(workspace);
    }

    @Test
    void deveLancarExcecaoQuandoNomeDoWorkspaceJaEstaCadastradoParaEsseUsuario() {

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                "Workspace",
                "Descrição",
                null,
                null
        );

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(workspaceRepository.existsByNomeAndIdUsuario(request.nome(), 1L))
                .thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(request)
        );

        assertEquals(
                "Já existe um workspace com esse nome para este usuário",
                exception.getMessage()
        );

        verify(authService).getAuthenticatedUser();
        verify(workspaceRepository).existsByNomeAndIdUsuario(request.nome(), 1L);
        verify(corWorkspaceRepository, never()).findByIdAtivo(anyLong());
        verify(iconeWorkspaceRepository, never()).findByIdAtivo(anyLong());
        verify(workspaceRepository, never()).save(any());
        verify(mapper, never()).toDomain(any(), any(), any(), anyLong());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void deveLancarExcecaoQuandoCorNaoForEncontrada() {

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                "Workspace",
                "Descrição",
                10L,
                null
        );

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(workspaceRepository.existsByNomeAndIdUsuario(request.nome(), 1L))
                .thenReturn(false);
        when(corWorkspaceRepository.findByIdAtivo(10L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(request)
        );

        assertEquals(
                "Cor com id: 10 não encontrada",
                exception.getMessage()
        );

        verify(authService).getAuthenticatedUser();
        verify(workspaceRepository).existsByNomeAndIdUsuario(request.nome(), 1L);
        verify(corWorkspaceRepository).findByIdAtivo(10L);
        verify(iconeWorkspaceRepository, never()).findByIdAtivo(anyLong());
        verify(workspaceRepository, never()).save(any());
        verify(mapper, never()).toDomain(any(), any(), any(), anyLong());
    }

    @Test
    void deveLancarExcecaoQuandoIconeNaoForEncontrado() {

        WorkspaceRequestDTO request = new WorkspaceRequestDTO(
                "Workspace",
                "Descrição",
                null,
                20L
        );

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(workspaceRepository.existsByNomeAndIdUsuario(request.nome(), 1L))
                .thenReturn(false);
        when(iconeWorkspaceRepository.findByIdAtivo(20L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(request)
        );

        assertEquals(
                "Ícone com id: 20 não encontrado",
                exception.getMessage()
        );

        verify(authService).getAuthenticatedUser();
        verify(workspaceRepository).existsByNomeAndIdUsuario(request.nome(), 1L);
        verify(corWorkspaceRepository, never()).findByIdAtivo(anyLong());
        verify(iconeWorkspaceRepository).findByIdAtivo(20L);
        verify(workspaceRepository, never()).save(any());
        verify(mapper, never()).toDomain(any(), any(), any(), anyLong());
    }
}