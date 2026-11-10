package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.ResumoWorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarResumoWorkspaceUseCaseTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private BuscarResumoWorkspaceUseCase useCase;

    @Test
    void deveRetornarTotalEQuantidadePorStatus() {

        Workspace workspace = new WorkspaceBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .build();

        when(workspaceRepository.findByIdAtivo(1L)).thenReturn(Optional.of(workspace));
        when(tarefaRepository.countAtivasByIdWorkspace(1L)).thenReturn(12L);
        when(tarefaRepository.countAtivasByIdWorkspaceAndStatus(1L, StatusTarefa.CONCLUIDA)).thenReturn(4L);
        when(tarefaRepository.countAtivasByIdWorkspaceAndStatus(1L, StatusTarefa.EM_ANDAMENTO)).thenReturn(3L);
        when(tarefaRepository.countAtivasByIdWorkspaceAndStatus(1L, StatusTarefa.PENDENTE)).thenReturn(4L);
        when(tarefaRepository.countAtivasByIdWorkspaceAndStatus(1L, StatusTarefa.ATRASADA)).thenReturn(1L);

        ResumoWorkspaceResponseDTO resumo = useCase.execute(1L);

        assertEquals(12L, resumo.total());
        assertEquals(4L, resumo.concluidas());
        assertEquals(3L, resumo.emAndamento());
        assertEquals(4L, resumo.pendentes());
        assertEquals(1L, resumo.atrasadas());
        verify(authService).validateOwnerOrAdmin(10L);
    }

    @Test
    void deveLancarExcecaoQuandoWorkspaceNaoEncontrado() {

        when(workspaceRepository.findByIdAtivo(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(99L)
        );

        assertEquals("Workspace com id: 99 não encontrado.", exception.getMessage());
        verifyNoInteractions(tarefaRepository);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
    }

    @Test
    void naoDeveContarTarefasQuandoUsuarioNaoTemPermissao() {

        Workspace workspace = new WorkspaceBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .build();

        when(workspaceRepository.findByIdAtivo(1L)).thenReturn(Optional.of(workspace));
        org.mockito.Mockito.doThrow(new RuntimeException("Acesso negado"))
                .when(authService).validateOwnerOrAdmin(10L);

        assertThrows(RuntimeException.class, () -> useCase.execute(1L));

        verify(tarefaRepository, never()).countAtivasByIdWorkspaceAndStatus(anyLong(), any());
    }
}
