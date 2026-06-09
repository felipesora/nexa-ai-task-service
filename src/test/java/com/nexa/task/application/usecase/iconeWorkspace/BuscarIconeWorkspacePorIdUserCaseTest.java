package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.IconeWorkspaceControllerMapper;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarIconeWorkspacePorIdUserCaseTest {

    @Mock
    private IconeWorkspaceRepository repository;

    @Mock
    private IconeWorkspaceControllerMapper mapper;

    @InjectMocks
    private BuscarIconeWorkspacePorIdUserCase useCase;

    @Test
    void deveBuscarIconePorIdComSucesso() {
        // Arrange
        Long id = 1L;

        IconeWorkspace icone = new IconeWorkspaceBuilder()
                .comId(id)
                .comNome("Ícone padrão")
                .comCaminho("icone-padrao.png")
                .comAtivo(true)
                .build();

        IconeWorkspaceResponseDTO response = new IconeWorkspaceResponseDTO(
                icone.getId(),
                icone.getNome(),
                icone.getCaminho(),
                icone.getAtivo()
        );

        when(repository.findById(id))
                .thenReturn(Optional.of(icone));

        when(mapper.toResponse(icone))
                .thenReturn(response);

        // Act
        IconeWorkspaceResponseDTO resultado = useCase.execute(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("Ícone padrão", resultado.nome());

        verify(repository).findById(id);
        verify(mapper).toResponse(icone);
    }

    @Test
    void deveLancarExcecaoQuandoIconeNaoForEncontrado() {
        // Arrange
        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(id)
        );

        assertEquals(
                "Ícone com id: 999 não encontrado.",
                exception.getMessage()
        );

        verify(repository).findById(id);
        verify(mapper, never()).toResponse(any());
    }
}