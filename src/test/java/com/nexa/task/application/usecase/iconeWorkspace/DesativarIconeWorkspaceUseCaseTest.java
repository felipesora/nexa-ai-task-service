package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.workspace.IconeWorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
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
class DesativarIconeWorkspaceUseCaseTest {

    @Mock
    private IconeWorkspaceRepository repository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @InjectMocks
    private DesativarIconeWorkspaceUseCase useCase;

    @Test
    void deveDesativarIconeComSucesso() {

        Long id = 1L;

        IconeWorkspace icone = new IconeWorkspaceBuilder()
                .comId(id)
                .comNome("Ícone")
                .comCaminho("icone.png")
                .comAtivo(true)
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(icone));

        assertTrue(icone.getAtivo());

        useCase.execute(id);

        assertFalse(icone.getAtivo());

        verify(repository).findById(id);
        verify(workspaceRepository).removerIconeDosWorkspaces(id);
        verify(repository).save(icone);
    }

    @Test
    void deveLancarExcecaoQuandoIconeNaoExistir() {

        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(id)
        );

        assertEquals(
                "Ícone com id: 999 não encontrado.",
                exception.getMessage()
        );

        verify(repository).findById(id);
        verifyNoInteractions(workspaceRepository);
        verify(repository, never()).save(any());
    }
}