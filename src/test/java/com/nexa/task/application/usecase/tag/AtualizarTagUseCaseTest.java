package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagUpdateDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.tag.CorTagBuilder;
import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarTagUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private CorTagRepository corTagRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private AtualizarTagUseCase useCase;

    @Test
    void deveAtualizarTagComSucessoSemCor() {
        Long idTag = 1L;

        TagUpdateDTO request = new TagUpdateDTO(
                "Tag Atualizada",
                null
        );

        Tag tag = new TagBuilder()
                .comId(idTag)
                .comIdUsuario(1L)
                .comNome("Tag Antiga")
                .build();

        when(tagRepository.findById(idTag)).thenReturn(Optional.of(tag));
        when(tagRepository.existsByNomeAndIdUsuarioAndIdNot(
                "Tag Atualizada",
                1L,
                idTag
        )).thenReturn(false);

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        useCase.execute(idTag, request);

        verify(authService).validateOwnerOrAdmin(1L);
        verify(tagRepository).save(tag);

        assertEquals("Tag Atualizada", tag.getNome());
        assertNull(tag.getCorTag());
    }

    @Test
    void deveAtualizarTagComSucessoComCor() {
        Long idTag = 1L;

        TagUpdateDTO request = new TagUpdateDTO(
                "Tag Atualizada",
                10L
        );

        Tag tag = new TagBuilder()
                .comId(idTag)
                .comIdUsuario(1L)
                .comNome("Tag Antiga")
                .comCor(null)
                .build();

        CorTag corTag = new CorTagBuilder()
                .comId(10L)
                .comCor("#123456")
                .build();

        when(tagRepository.findById(idTag)).thenReturn(Optional.of(tag));
        when(tagRepository.existsByNomeAndIdUsuarioAndIdNot(
                "Tag Atualizada",
                1L,
                idTag
        )).thenReturn(false);
        when(corTagRepository.findByIdAtivo(10L)).thenReturn(Optional.of(corTag));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        useCase.execute(idTag, request);

        verify(authService).validateOwnerOrAdmin(1L);
        verify(tagRepository).save(tag);

        assertEquals("Tag Atualizada", tag.getNome());
        assertEquals(corTag, tag.getCorTag());
    }

    @Test
    void deveLancarExcecaoQuandoTagNaoForEncontrada() {
        Long idTag = 999L;

        TagUpdateDTO request = new TagUpdateDTO(
                "Tag Atualizada",
                null
        );

        when(tagRepository.findById(idTag)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(idTag, request)
        );

        assertEquals("Tag com id: 999 não encontrada.", exception.getMessage());

        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(tagRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCorNaoForEncontrada() {
        Long idTag = 1L;

        TagUpdateDTO request = new TagUpdateDTO(
                "Tag Atualizada",
                99L
        );

        Tag tag = new TagBuilder()
                .comId(idTag)
                .comIdUsuario(1L)
                .build();

        when(tagRepository.findById(idTag)).thenReturn(Optional.of(tag));
        when(tagRepository.existsByNomeAndIdUsuarioAndIdNot(
                "Tag Atualizada",
                1L,
                idTag
        )).thenReturn(false);
        when(corTagRepository.findByIdAtivo(99L)).thenReturn(Optional.empty());

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(idTag, request)
        );

        assertEquals("Cor com id: 99 não encontrada", exception.getMessage());

        verify(authService).validateOwnerOrAdmin(1L);
        verify(tagRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExistirParaUsuarioAoAtualizar() {
        Long idTag = 1L;

        TagUpdateDTO request = new TagUpdateDTO(
                "Tag Atualizada",
                10L
        );

        Tag tag = new TagBuilder()
                .comId(idTag)
                .comIdUsuario(1L)
                .comNome("Tag Antiga")
                .build();

        when(tagRepository.findById(idTag)).thenReturn(Optional.of(tag));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        when(tagRepository.existsByNomeAndIdUsuarioAndIdNot(
                "Tag Atualizada",
                1L,
                idTag
        )).thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(idTag, request)
        );

        assertEquals("Já existe uma tag com esse nome para este usuário", exception.getMessage());

        verify(authService).validateOwnerOrAdmin(1L);
        verify(corTagRepository, never()).findByIdAtivo(anyLong());
        verify(tagRepository, never()).save(any());
    }
}
