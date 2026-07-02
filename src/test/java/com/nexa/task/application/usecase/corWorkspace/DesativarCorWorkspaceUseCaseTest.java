package com.nexa.task.application.usecase.corWorkspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.workspace.CorWorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
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
class DesativarCorWorkspaceUseCaseTest {

    @Mock
    private CorWorkspaceRepository repository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private DesativarCorWorkspaceUseCase useCase;

    @Test
    void deveDesativarCorComSucesso() {

        Long id = 1L;

        CorWorkspace cor = new CorWorkspaceBuilder()
                .comId(id)
                .comCor("#0000FF")
                .comAtivo(true)
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(cor));

        assertTrue(cor.getAtivo());

        useCase.execute(id);

        assertFalse(cor.getAtivo());

        verify(repository).findById(id);
        verify(workspaceRepository).removerCorDosWorkspaces(id);
        verify(repository).save(cor);
    }

    @Test
    void deveLancarExcecaoQuandoCorNaoExistir() {

        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(id)
        );

        assertEquals(
                "Cor do Workspace com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(repository).findById(id);
        verifyNoInteractions(workspaceRepository);
        verify(repository, never()).save(any());
    }
}