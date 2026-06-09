package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarIconeWorkspaceUseCaseTest {

    @Mock
    private IconeWorkspaceRepository repository;

    @Mock
    private IconeWorkspaceControllerMapper mapper;

    @InjectMocks
    private CadastrarIconeWorkspaceUseCase useCase;

    @Test
    void deveCadastrarIconeComSucesso() {
        // Arrange
        IconeWorkspaceRequestDTO request = new IconeWorkspaceRequestDTO("Dashboard", "dashboard.svg");

        IconeWorkspace icone = new IconeWorkspaceBuilder()
                .comId(1L)
                .comNome("Dashboard")
                .comCaminho("dashboard.svg")
                .comAtivo(true)
                .build();

        IconeWorkspace salvo = new IconeWorkspaceBuilder()
                .comId(1L)
                .comNome("Dashboard")
                .comCaminho("dashboard.svg")
                .comAtivo(true)
                .build();

        IconeWorkspaceResponseDTO response = new IconeWorkspaceResponseDTO(1L, "Dashboard", "dashboard.svg", true);

        when(repository.findByNome("Dashboard")).thenReturn(Optional.empty());

        when(repository.findByCaminho("dashboard.svg")).thenReturn(Optional.empty());

        when(mapper.toDomain(request)).thenReturn(icone);

        when(repository.save(icone)).thenReturn(salvo);

        when(mapper.toResponse(salvo)).thenReturn(response);

        // Act
        IconeWorkspaceResponseDTO resultado = useCase.execute(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(response, resultado);

        verify(repository).findByNome("Dashboard");
        verify(repository).findByCaminho("dashboard.svg");
        verify(repository).save(icone);
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExistir() {
        // Arrange
        IconeWorkspaceRequestDTO request = new IconeWorkspaceRequestDTO("Dashboard", "dashboard.svg");

        when(repository.findByNome("Dashboard")).thenReturn(Optional.of(new IconeWorkspaceBuilder().build()));

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(request)
        );

        assertEquals("Este nome de ícone já está cadastrado.", exception.getMessage());

        verify(repository).findByNome("Dashboard");
        verify(repository, never()).findByCaminho(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCaminhoJaExistir() {
        // Arrange
        IconeWorkspaceRequestDTO request = new IconeWorkspaceRequestDTO("Dashboard", "dashboard.svg");

        when(repository.findByNome("Dashboard")).thenReturn(Optional.empty());

        when(repository.findByCaminho("dashboard.svg")).thenReturn(Optional.of(new IconeWorkspaceBuilder().build()));

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(request)
        );

        assertEquals("Este caminho de ícone já está cadastrado.", exception.getMessage());

        verify(repository).findByNome("Dashboard");
        verify(repository).findByCaminho("dashboard.svg");
        verify(repository, never()).save(any());
    }
}