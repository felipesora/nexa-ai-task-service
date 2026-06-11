package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.workspace.IconeWorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtivarIconeWorkspaceUseCaseTest {

    @Mock
    private IconeWorkspaceRepository repository;

    @InjectMocks
    private AtivarIconeWorkspaceUseCase useCase;

    @Test
    void deveAtivarIconeComSucesso() {

        Long id = 1L;

        IconeWorkspace icone = new IconeWorkspaceBuilder()
                .comId(id)
                .comNome("Ícone")
                .comCaminho("icone.png")
                .comAtivo(false)
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(icone));

        useCase.execute(id);

        assertTrue(icone.getAtivo());

        verify(repository).findById(id);
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
        verify(repository, never()).save(any());
    }
}