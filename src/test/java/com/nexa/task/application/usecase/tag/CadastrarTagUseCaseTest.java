package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagCreateDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.builder.tag.CorTagBuilder;
import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarTagUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private CorTagRepository corTagRepository;

    @Mock
    private TagControllerMapper mapper;

    @InjectMocks
    private CadastrarTagUseCase useCase;

    @Test
    void deveCadastrarTagComSucessoSemCor() {
        TagCreateDTO request = new TagCreateDTO(
                1L,
                "Urgente",
                null
        );

        Tag tag = new TagBuilder()
                .comId(1L)
                .comIdUsuario(1L)
                .comNome("Urgente")
                .comCor(null)
                .build();

        TagResponseDTO response = new TagResponseDTO(
                1L,
                1L,
                "Urgente",
                true,
                null
        );

        when(mapper.toDomain(request, null)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(mapper.toResponse(tag)).thenReturn(response);

        TagResponseDTO resultado = useCase.execute(request);

        assertNotNull(resultado);
        assertEquals(response, resultado);
    }

    @Test
    void deveCadastrarTagComSucessoComCor() {
        TagCreateDTO request = new TagCreateDTO(
                1L,
                "Estudo",
                10L
        );

        CorTag corTag = new CorTagBuilder()
                .comId(10L)
                .comCor("#00FF00")
                .build();

        Tag tag = new TagBuilder()
                .comId(1L)
                .comIdUsuario(1L)
                .comNome("Estudo")
                .comCor(corTag)
                .build();

        TagResponseDTO response = new TagResponseDTO(
                1L,
                1L,
                "Estudo",
                true,
                null
        );

        when(corTagRepository.findById(10L)).thenReturn(Optional.of(corTag));
        when(mapper.toDomain(request, corTag)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(mapper.toResponse(tag)).thenReturn(response);

        TagResponseDTO resultado = useCase.execute(request);

        assertNotNull(resultado);
        assertEquals(response, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoCorNaoForEncontrada() {
        TagCreateDTO request = new TagCreateDTO(
                1L,
                "Importante",
                99L
        );

        when(corTagRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(request)
        );

        assertEquals("Cor com id: 99 não encontrada", exception.getMessage());
    }
}
