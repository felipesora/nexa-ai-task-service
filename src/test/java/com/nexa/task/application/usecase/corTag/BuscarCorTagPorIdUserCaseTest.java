package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.CorTagControllerMapper;
import com.nexa.task.domain.builder.tag.CorTagBuilder;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.repository.CorTagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarCorTagPorIdUserCaseTest {

    @Mock
    private CorTagRepository repository;

    @Mock
    private CorTagControllerMapper mapper;

    @InjectMocks
    private BuscarCorTagPorIdUserCase useCase;

    @Test
    void deveBuscarCorTagPorIdComSucesso() {

        Long id = 1L;

        CorTag cor = new CorTagBuilder()
                .comId(id)
                .comCor("#FF0000")
                .comAtivo(true)
                .build();

        CorTagResponseDTO response =
                new CorTagResponseDTO(
                        id,
                        "#FF0000",
                        true
                );

        when(repository.findById(id))
                .thenReturn(Optional.of(cor));

        when(mapper.toResponse(cor))
                .thenReturn(response);

        CorTagResponseDTO resultado =
                useCase.execute(id);

        assertEquals(id, resultado.id());

        verify(repository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoCorTagNaoExistir() {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(999L)
        );

        assertEquals(
                "Cor da Tag com id: 999 não encontrada.",
                exception.getMessage()
        );
    }
}