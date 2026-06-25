package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.subtarefa.SubtarefaBuilder;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.subtarefa.Subtarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.SubtarefaRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesativarWorkspaceUseCaseTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @InjectMocks
    private DesativarWorkspaceUseCase useCase;

    @Test
    void deveDesativarWorkspaceComSucesso() {

        Workspace workspace = new WorkspaceBuilder()
                .comId(1L)
                .comNome("Workspace Teste")
                .comAtivo(true)
                .build();

        Tarefa tarefa = new TarefaBuilder()
                .comId(10L)
                .comAtivo(true)
                .build();

        Subtarefa subtarefa = new SubtarefaBuilder()
                .comId(100L)
                .comAtivo(true)
                .build();

        when(workspaceRepository.findById(1L))
                .thenReturn(Optional.of(workspace));

        when(tarefaRepository.findAllByWorkspace(1L))
                .thenReturn(List.of(tarefa));

        when(subtarefaRepository.findAllByTarefa(10L))
                .thenReturn(List.of(subtarefa));

        useCase.execute(1L);

        assertFalse(workspace.getAtivo());
        assertFalse(tarefa.getAtivo());
        assertFalse(subtarefa.getAtivo());

        verify(workspaceRepository).findById(1L);
        verify(tarefaRepository).findAllByWorkspace(1L);
        verify(subtarefaRepository).findAllByTarefa(10L);

        verify(subtarefaRepository).save(subtarefa);
        verify(tarefaRepository).save(tarefa);
        verify(workspaceRepository).save(workspace);
    }

    @Test
    void deveLancarExcecaoQuandoWorkspaceNaoEncontrado() {

        when(workspaceRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> useCase.execute(999L));

        assertEquals(
                "Workspace com id: 999 não encontrado",
                exception.getMessage()
        );

        verify(workspaceRepository).findById(999L);

        verifyNoInteractions(tarefaRepository);
        verifyNoInteractions(subtarefaRepository);

        verify(workspaceRepository, never()).save(any());
    }
}