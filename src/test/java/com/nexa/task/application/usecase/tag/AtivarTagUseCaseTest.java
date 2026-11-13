package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtivarTagUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private AtivarTagUseCase useCase;

    @Test
    void deveAtivarTagComSucesso() {

        Tag tag = new TagBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .comNome("Tag Teste")
                .comAtivo(false)
                .build();

        when(tagRepository.findById(1L))
                .thenReturn(Optional.of(tag));
        when(tagRepository.existsAtivoByNomeAndIdUsuarioAndIdNot("Tag Teste", 10L, 1L))
                .thenReturn(false);

        useCase.execute(1L);

        assertTrue(tag.getAtivo());

        verify(tagRepository).findById(1L);
        verify(tagRepository).existsAtivoByNomeAndIdUsuarioAndIdNot("Tag Teste", 10L, 1L);
        verify(tagRepository).save(tag);
    }

    @Test
    void deveLancarExcecaoQuandoJaExisteTagAtivaComMesmoNome() {

        Tag tag = new TagBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .comNome("Tag Teste")
                .comAtivo(false)
                .build();

        when(tagRepository.findById(1L))
                .thenReturn(Optional.of(tag));
        when(tagRepository.existsAtivoByNomeAndIdUsuarioAndIdNot("Tag Teste", 10L, 1L))
                .thenReturn(true);

        BadRequestException exception =
                assertThrows(BadRequestException.class,
                        () -> useCase.execute(1L));

        assertEquals(
                "Já existe uma tag ativa com esse nome para este usuário",
                exception.getMessage()
        );

        assertFalse(tag.getAtivo());
        verify(tagRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTagNaoEncontrada() {

        when(tagRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> useCase.execute(999L));

        assertEquals(
                "Tag com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(tagRepository).findById(999L);
        verify(tagRepository, never()).save(any());
    }
}
