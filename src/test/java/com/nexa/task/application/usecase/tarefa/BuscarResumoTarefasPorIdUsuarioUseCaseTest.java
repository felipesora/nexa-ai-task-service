package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.ResumoTarefasResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaPrioritariaResponseDTO;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarResumoTarefasPorIdUsuarioUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private BuscarResumoTarefasPorIdUsuarioUseCase useCase;

    @Test
    void deveRetornarQuantidadesPorStatusETarefasPrioritarias() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comTitulo("Tarefa urgente")
                .build();

        TarefaPrioritariaResponseDTO prioritaria = new TarefaPrioritariaResponseDTO(
                1L, "Tarefa urgente", PrioridadeTarefa.URGENTE, StatusTarefa.PENDENTE, null, 10L, "Meu Workspace"
        );

        when(tarefaRepository.countAtivasByIdUsuarioAndStatus(1L, StatusTarefa.CONCLUIDA)).thenReturn(18L);
        when(tarefaRepository.countAtivasByIdUsuarioAndStatus(1L, StatusTarefa.EM_ANDAMENTO)).thenReturn(7L);
        when(tarefaRepository.countAtivasByIdUsuarioAndStatus(1L, StatusTarefa.PENDENTE)).thenReturn(5L);
        when(tarefaRepository.countAtrasadasByIdUsuario(1L)).thenReturn(2L);
        when(tarefaRepository.findPrioritariasByIdUsuario(1L, 5)).thenReturn(List.of(tarefa));
        when(mapper.toPrioritariaResponse(tarefa)).thenReturn(prioritaria);

        ResumoTarefasResponseDTO resumo = useCase.execute(1L);

        assertEquals(18L, resumo.concluidas());
        assertEquals(7L, resumo.emAndamento());
        assertEquals(5L, resumo.pendentes());
        assertEquals(2L, resumo.atrasadas());
        assertEquals(List.of(prioritaria), resumo.tarefasPrioritarias());

        verify(authService).validateOwnerOrAdmin(1L);
        verify(tarefaRepository).findPrioritariasByIdUsuario(1L, 5);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaTarefasEmAberto() {

        when(tarefaRepository.countAtivasByIdUsuarioAndStatus(anyLong(), any())).thenReturn(0L);
        when(tarefaRepository.findPrioritariasByIdUsuario(1L, 5)).thenReturn(List.of());

        ResumoTarefasResponseDTO resumo = useCase.execute(1L);

        assertEquals(0L, resumo.concluidas());
        assertTrue(resumo.tarefasPrioritarias().isEmpty());
    }

    @Test
    void naoDeveConsultarTarefasQuandoUsuarioNaoTemPermissao() {

        doThrow(new AccessDeniedException("Acesso negado"))
                .when(authService).validateOwnerOrAdmin(2L);

        assertThrows(AccessDeniedException.class, () -> useCase.execute(2L));

        verify(tarefaRepository, never()).countAtivasByIdUsuarioAndStatus(anyLong(), any());
        verify(tarefaRepository, never()).findPrioritariasByIdUsuario(anyLong(), anyInt());
    }
}
