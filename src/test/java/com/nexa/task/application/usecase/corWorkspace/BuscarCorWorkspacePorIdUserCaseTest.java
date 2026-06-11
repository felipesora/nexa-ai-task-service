package com.nexa.task.application.usecase.corWorkspace;

import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.CorWorkspaceControllerMapper;
import com.nexa.task.domain.builder.workspace.CorWorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarCorWorkspacePorIdUserCaseTest {

    @Mock
    private CorWorkspaceRepository repository;

    @Mock
    private CorWorkspaceControllerMapper mapper;

    @InjectMocks
    private BuscarCorWorkspacePorIdUserCase useCase;

    @Test
    void deveBuscarCorPorIdComSucesso() {

        Long id = 1L;

        CorWorkspace cor = new CorWorkspaceBuilder()
                .comId(id)
                .comCor("#0000FF")
                .comAtivo(true)
                .build();

        CorWorkspaceResponseDTO response =
                new CorWorkspaceResponseDTO(
                        id,
                        "#0000FF",
                        true
                );

        when(repository.findById(id))
                .thenReturn(Optional.of(cor));

        when(mapper.toResponse(cor))
                .thenReturn(response);

        CorWorkspaceResponseDTO resultado =
                useCase.execute(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("#0000FF", resultado.cor());

        verify(repository).findById(id);
        verify(mapper).toResponse(cor);
    }

    @Test
    void deveLancarExcecaoQuandoCorNaoForEncontrada() {

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
        verify(mapper, never()).toResponse(any());
    }
}