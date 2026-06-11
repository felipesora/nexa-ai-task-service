package com.nexa.task.application.usecase.workspace;

import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.mapper.WorkspaceControllerMapper;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarWorkspacesPorIdUsuarioENomeUseCaseTest {


    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WorkspaceControllerMapper mapper;

    @InjectMocks
    private ListarWorkspacesPorIdUsuarioENomeUseCase useCase;

    @Test
    void deveListarWorkspacesPorIdUsuarioENomeComPaginacao() {

        // Arrange
        Long idUsuario = 1L;
        String nome = "Workspace Financeiro";

        Pageable pageable = PageRequest.of(0, 10);

        Workspace workspace = new WorkspaceBuilder()
                .comId(1L)
                .comIdUsuario(idUsuario)
                .comNome(nome)
                .build();

        Page<Workspace> pageEntity = new PageImpl<>(
                List.of(workspace),
                pageable,
                1
        );

        WorkspaceResponseDTO response = new WorkspaceResponseDTO(
                1L,
                idUsuario,
                nome,
                "Descrição",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null,
                null
        );

        when(workspaceRepository.findByIdUsuarioAndNome(
                idUsuario,
                nome,
                pageable))
                .thenReturn(pageEntity);

        when(mapper.toResponse(workspace))
                .thenReturn(response);

        // Act
        Page<WorkspaceResponseDTO> resultado =
                useCase.execute(idUsuario, nome, pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());

        WorkspaceResponseDTO workspaceRetornado =
                resultado.getContent().get(0);

        assertEquals(1L, workspaceRetornado.id());
        assertEquals(idUsuario, workspaceRetornado.idUsuario());
        assertEquals(nome, workspaceRetornado.nome());

        verify(workspaceRepository)
                .findByIdUsuarioAndNome(idUsuario, nome, pageable);

        verify(mapper)
                .toResponse(workspace);
    }
}