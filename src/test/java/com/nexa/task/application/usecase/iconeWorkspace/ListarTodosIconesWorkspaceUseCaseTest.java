package com.nexa.task.application.usecase.iconeWorkspace;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.mapper.IconeWorkspaceControllerMapper;
import com.nexa.task.domain.builder.workspace.IconeWorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import com.nexa.task.domain.repository.IconeWorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarTodosIconesWorkspaceUseCaseTest {

    @Mock
    private IconeWorkspaceRepository repository;

    @Mock
    private IconeWorkspaceControllerMapper mapper;

    @InjectMocks
    private ListarTodosIconesWorkspaceUseCase useCase;

    @Test
    void deveListarTodosOsIconesComPaginacao() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        IconeWorkspace icone1 = new IconeWorkspaceBuilder()
                .comId(1L)
                .comNome("Ícone 1")
                .comCaminho("icone1.png")
                .comAtivo(true)
                .build();

        IconeWorkspace icone2 = new IconeWorkspaceBuilder()
                .comId(2L)
                .comNome("Ícone 2")
                .comCaminho("icone2.png")
                .comAtivo(true)
                .build();

        Page<IconeWorkspace> pageEntity = new PageImpl<>(
                List.of(icone1, icone2),
                pageable,
                2
        );

        IconeWorkspaceResponseDTO response1 =
                new IconeWorkspaceResponseDTO(
                        1L,
                        "Ícone 1",
                        "icone1.png",
                        true
                );

        IconeWorkspaceResponseDTO response2 =
                new IconeWorkspaceResponseDTO(
                        2L,
                        "Ícone 2",
                        "icone2.png",
                        true
                );

        when(repository.findAll(pageable))
                .thenReturn(pageEntity);

        when(mapper.toResponse(icone1))
                .thenReturn(response1);

        when(mapper.toResponse(icone2))
                .thenReturn(response2);

        // Act
        Page<IconeWorkspaceResponseDTO> resultado = useCase.execute(pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(1, resultado.getTotalPages());
        assertEquals(0, resultado.getNumber());
        assertEquals(10, resultado.getSize());

        assertEquals("Ícone 1", resultado.getContent().get(0).nome());
        assertEquals("Ícone 2", resultado.getContent().get(1).nome());

        verify(repository).findAll(pageable);
        verify(mapper).toResponse(icone1);
        verify(mapper).toResponse(icone2);
    }
}