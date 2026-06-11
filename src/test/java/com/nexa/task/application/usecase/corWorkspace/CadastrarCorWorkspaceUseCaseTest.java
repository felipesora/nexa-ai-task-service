package com.nexa.task.application.usecase.corWorkspace;

import com.nexa.task.application.dto.corWorkspace.CorWorkspaceRequestDTO;
import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarCorWorkspaceUseCaseTest {

    @Mock
    private CorWorkspaceRepository repository;

    @Mock
    private CorWorkspaceControllerMapper mapper;

    @InjectMocks
    private CadastrarCorWorkspaceUseCase useCase;

    @Test
    void deveCadastrarCorComSucesso() {
        // Arrange
        CorWorkspaceRequestDTO request = new CorWorkspaceRequestDTO("#FFFFFF");

        CorWorkspace cor = new CorWorkspaceBuilder()
                .comId(1L)
                .comCor("#FFFFFF")
                .comAtivo(true)
                .build();

        CorWorkspace salvo = new CorWorkspaceBuilder()
                .comId(1L)
                .comCor("#FFFFFF")
                .comAtivo(true)
                .build();

        CorWorkspaceResponseDTO response = new CorWorkspaceResponseDTO(1L, "#FFFFFF", true);;

        when(repository.findByCor("#FFFFFF")).thenReturn(Optional.empty());

        when(mapper.toDomain(request)).thenReturn(cor);

        when(repository.save(cor)).thenReturn(salvo);

        when(mapper.toResponse(salvo)).thenReturn(response);

        // Act
        CorWorkspaceResponseDTO resultado = useCase.execute(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(response, resultado);

        verify(repository).findByCor("#FFFFFF");
        verify(repository).save(cor);
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExistir() {
        // Arrange
        CorWorkspaceRequestDTO request = new CorWorkspaceRequestDTO("#FFFFFF");

        when(repository.findByCor("#FFFFFF")).thenReturn(Optional.of(new CorWorkspaceBuilder().build()));

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(request)
        );

        assertEquals("Esta cor de workspace já está cadastrada.", exception.getMessage());

        verify(repository).findByCor("#FFFFFF");
        verify(repository, never()).save(any());
    }
}