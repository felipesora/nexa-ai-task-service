package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tarefa.FiltroTarefas;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import com.nexa.task.infra.security.AuthenticationService;
import com.nexa.task.infra.security.ForbiddenException;
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

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private ListarTarefasPorIdWorkspaceUseCase useCase;

    private final FiltroTarefas filtro = FiltroTarefas.vazio();

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

        when(workspaceRepository.findByIdAtivo(1L))
                .thenReturn(Optional.of(workspace));

        when(workspace.getIdUsuario()).thenReturn(1L);
        doNothing().when(authService).validateOwnerOrAdmin(1L);

        when(tarefaRepository.findByIdWorkspaceAndAtivo(1L, filtro, pageable))
                .thenReturn(page);

        when(mapper.toResponse(tarefa))
                .thenReturn(response);

        Page<TarefaResponseDTO> resultado =
                useCase.execute(1L, filtro, pageable);

        assertEquals(1, resultado.getTotalElements());

        verify(workspaceRepository).findByIdAtivo(1L);
        verify(authService).validateOwnerOrAdmin(1L);
        verify(tarefaRepository).findByIdWorkspaceAndAtivo(1L, filtro, pageable);
        verify(mapper).toResponse(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoWorkspaceNaoForEncontrado() {

        when(workspaceRepository.findByIdAtivo(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> useCase.execute(999L, filtro, pageable)
                );

        assertEquals(
                "Workspace com id: 999 não encontrado.",
                exception.getMessage()
        );

        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(tarefaRepository, never()).findByIdWorkspaceAndAtivo(anyLong(), any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoTemPermissao() {

        when(workspaceRepository.findByIdAtivo(1L))
                .thenReturn(Optional.of(workspace));

        when(workspace.getIdUsuario()).thenReturn(1L);

        doThrow(new ForbiddenException("Acesso negado"))
                .when(authService)
                .validateOwnerOrAdmin(1L);

        assertThrows(
                ForbiddenException.class,
                () -> useCase.execute(1L, filtro, pageable)
        );

        verify(tarefaRepository, never())
                .findByIdWorkspaceAndAtivo(anyLong(), any(), any());

        verify(mapper, never())
                .toResponse(any());
    }

    @Test
    void deveRepassarOsFiltrosParaORepositorio() {

        Pageable pageable = PageRequest.of(0, 10);
        FiltroTarefas filtroCompleto = new FiltroTarefas("relatorio", StatusTarefa.PENDENTE, PrioridadeTarefa.ALTA, 3L);
        Workspace workspaceDoUsuario = new com.nexa.task.domain.builder.workspace.WorkspaceBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .build();

        when(workspaceRepository.findByIdAtivo(1L)).thenReturn(Optional.of(workspaceDoUsuario));
        when(tarefaRepository.findByIdWorkspaceAndAtivo(1L, filtroCompleto, pageable))
                .thenReturn(new PageImpl<>(List.of(), pageable, 0));

        Page<TarefaResponseDTO> resultado = useCase.execute(1L, filtroCompleto, pageable);

        assertEquals(0, resultado.getTotalElements());
        verify(tarefaRepository).findByIdWorkspaceAndAtivo(1L, filtroCompleto, pageable);
    }

    @Test
    void deveTratarTituloEmBrancoComoSemFiltro() {

        FiltroTarefas filtroComTituloEmBranco = new FiltroTarefas("   ", null, null, null);

        assertEquals(FiltroTarefas.vazio(), filtroComTituloEmBranco);
    }
}