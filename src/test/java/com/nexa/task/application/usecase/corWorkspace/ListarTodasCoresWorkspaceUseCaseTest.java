package com.nexa.task.application.usecase.corWorkspace;

import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.mapper.CorWorkspaceControllerMapper;
import com.nexa.task.domain.builder.workspace.CorWorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.domain.repository.CorWorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTodasCoresWorkspaceUseCaseTest {

    @Mock
    private CorWorkspaceRepository repository;

    @Mock
    private CorWorkspaceControllerMapper mapper;

    @InjectMocks
    private ListarTodasCoresWorkspaceUseCase useCase;

    @Test
    void deveListarTodasAsCoresComPaginacao() {

        Pageable pageable = PageRequest.of(0, 10);

        CorWorkspace cor1 = new CorWorkspaceBuilder()
                .comId(1L)
                .comCor("#0000FF")
                .comAtivo(true)
                .build();

        CorWorkspace cor2 = new CorWorkspaceBuilder()
                .comId(2L)
                .comCor("#FF0000")
                .comAtivo(true)
                .build();

        Page<CorWorkspace> pageEntity = new PageImpl<>(
                List.of(cor1, cor2),
                pageable,
                2
        );

        CorWorkspaceResponseDTO response1 =
                new CorWorkspaceResponseDTO(
                        1L,
                        "#0000FF",
                        true
                );

        CorWorkspaceResponseDTO response2 =
                new CorWorkspaceResponseDTO(
                        2L,
                        "#FF0000",
                        true
                );

        when(repository.findAll(pageable))
                .thenReturn(pageEntity);

        when(mapper.toResponse(cor1))
                .thenReturn(response1);

        when(mapper.toResponse(cor2))
                .thenReturn(response2);

        Page<CorWorkspaceResponseDTO> resultado =
                useCase.execute(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());

        verify(repository).findAll(pageable);
        verify(mapper).toResponse(cor1);
        verify(mapper).toResponse(cor2);
    }
}