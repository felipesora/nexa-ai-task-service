package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.Workspace;
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
class DesativarWorkspaceUseCaseTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private DesativarWorkspaceUseCase useCase;

    @Test
    void deveDesativarWorkspaceComSucesso() {

        Workspace workspace = new WorkspaceBuilder()
                .comId(1L)
                .comNome("Workspace Teste")
                .comAtivo(true)
                .build();

        when(workspaceRepository.findById(1L))
                .thenReturn(Optional.of(workspace));

        useCase.execute(1L);

        assertFalse(workspace.getAtivo());

        verify(workspaceRepository).findById(1L);
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
        verify(workspaceRepository, never()).save(any());
    }
}