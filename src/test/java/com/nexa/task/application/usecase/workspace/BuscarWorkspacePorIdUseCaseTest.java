package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarWorkspacePorIdUseCaseTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WorkspaceControllerMapper mapper;

    @InjectMocks
    private BuscarWorkspacePorIdUseCase useCase;

    @Test
    void deveBuscarWorkspacePorIdComSucesso() {

        Long id = 1L;

        Workspace workspace = new WorkspaceBuilder()
                .comId(id)
                .comIdUsuario(1L)
                .comNome("Meu Workspace")
                .comDescricao("Descrição")
                .comAtivo(true)
                .build();

        WorkspaceResponseDTO response = new WorkspaceResponseDTO(
                id,
                1L,
                "Meu Workspace",
                "Descrição",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null,
                null
        );

        when(workspaceRepository.findById(id))
                .thenReturn(Optional.of(workspace));

        when(mapper.toResponse(workspace))
                .thenReturn(response);

        WorkspaceResponseDTO resultado = useCase.execute(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("Meu Workspace", resultado.nome());
        assertEquals("Descrição", resultado.descricao());

        verify(workspaceRepository).findById(id);
        verify(mapper).toResponse(workspace);
    }

    @Test
    void deveLancarExcecaoQuandoWorkspaceNaoForEncontrado() {

        Long id = 999L;

        when(workspaceRepository.findById(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(id)
        );

        assertEquals(
                "Workspace com id: 999 não encontrado.",
                exception.getMessage()
        );

        verify(workspaceRepository).findById(id);
        verify(mapper, never()).toResponse(any());
    }
}