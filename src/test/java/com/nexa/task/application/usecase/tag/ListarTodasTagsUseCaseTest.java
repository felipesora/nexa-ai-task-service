package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.TagRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarTodasTagsUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagControllerMapper mapper;

    @InjectMocks
    private ListarTodasTagsUseCase useCase;

    @Test
    void deveListarTodasAsTagsComPaginacao() {

        Pageable pageable = PageRequest.of(0, 10);

        Tag tag1 = new TagBuilder()
                .comId(1L)
                .comNome("Urgente")
                .build();

        Tag tag2 = new TagBuilder()
                .comId(2L)
                .comNome("Estudo")
                .build();

        Page<Tag> pageEntity = new PageImpl<>(
                List.of(tag1, tag2),
                pageable,
                2
        );

        TagResponseDTO response1 = new TagResponseDTO(
                1L,
                1L,
                "Urgente",
                true,
                null
        );

        TagResponseDTO response2 = new TagResponseDTO(
                2L,
                1L,
                "Estudo",
                true,
                null
        );

        when(tagRepository.findAll(pageable))
                .thenReturn(pageEntity);

        when(mapper.toResponse(tag1))
                .thenReturn(response1);

        when(mapper.toResponse(tag2))
                .thenReturn(response2);

        Page<TagResponseDTO> resultado = useCase.execute(pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals("Urgente", resultado.getContent().get(0).nome());
        assertEquals("Estudo", resultado.getContent().get(1).nome());

        verify(tagRepository).findAll(pageable);
        verify(mapper).toResponse(tag1);
        verify(mapper).toResponse(tag2);
    }
}
