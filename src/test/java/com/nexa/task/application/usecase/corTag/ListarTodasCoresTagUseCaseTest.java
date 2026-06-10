package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.mapper.CorTagControllerMapper;
import com.nexa.task.domain.builder.tag.CorTagBuilder;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.repository.CorTagRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarTodasCoresTagUseCaseTest {

    @Mock
    private CorTagRepository repository;

    @Mock
    private CorTagControllerMapper mapper;

    @InjectMocks
    private ListarTodasCoresTagUseCase useCase;

    @Test
    void deveListarTodasAsCoresTagComPaginacao() {

        Pageable pageable = PageRequest.of(0, 10);

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

        Page<CorTag> page =
                new PageImpl<>(List.of(cor));

        when(repository.findAll(pageable))
                .thenReturn(page);

        when(mapper.toResponse(cor))
                .thenReturn(response);

        Page<CorTagResponseDTO> resultado =
                useCase.execute(pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals("#FF0000",
                resultado.getContent().get(0).cor());
    }
}