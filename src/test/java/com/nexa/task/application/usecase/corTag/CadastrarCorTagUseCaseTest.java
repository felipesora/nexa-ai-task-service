package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.dto.corTag.CorTagRequestDTO;
import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarCorTagUseCaseTest {

    @Mock
    private CorTagRepository repository;

    @Mock
    private CorTagControllerMapper mapper;

    @InjectMocks
    private CadastrarCorTagUseCase useCase;

    @Test
    void deveCadastrarCorTagComSucesso() {

        CorTagRequestDTO request =
                new CorTagRequestDTO("#FF0000");

        CorTag cor = new CorTagBuilder()
                .comId(1L)
                .comCor("#FF0000")
                .comAtivo(true)
                .build();

        CorTagResponseDTO response =
                new CorTagResponseDTO(
                        1L,
                        "#FF0000",
                        true
                );

        when(repository.findByCor("#FF0000"))
                .thenReturn(Optional.empty());

        when(mapper.toDomain(request))
                .thenReturn(cor);

        when(repository.save(cor))
                .thenReturn(cor);

        when(mapper.toResponse(cor))
                .thenReturn(response);

        CorTagResponseDTO resultado =
                useCase.execute(request);

        assertNotNull(resultado);
        assertEquals("#FF0000", resultado.cor());

        verify(repository).save(cor);
    }

    @Test
    void deveLancarExcecaoQuandoCorJaExistir() {

        CorTagRequestDTO request =
                new CorTagRequestDTO("#FF0000");

        when(repository.findByCor("#FF0000"))
                .thenReturn(Optional.of(mock(CorTag.class)));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(request)
        );

        assertEquals(
                "Esta cor de tag já está cadastrada.",
                exception.getMessage()
        );

        verify(repository, never()).save(any());
    }
}