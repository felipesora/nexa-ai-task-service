package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesativarTagUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private DesativarTagUseCase useCase;

    @Test
    void deveDesativarTagComSucesso() {

        Tag tag = new TagBuilder()
                .comId(1L)
                .comIdUsuario(1L)
                .comNome("Tag Teste")
                .comAtivo(true)
                .build();

        when(tagRepository.findById(1L))
                .thenReturn(Optional.of(tag));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        useCase.execute(1L);

        assertFalse(tag.getAtivo());

        verify(tagRepository).findById(1L);
        verify(authService).validateOwnerOrAdmin(1L);
        verify(tagRepository).save(tag);
    }

    @Test
    void deveLancarExcecaoQuandoTagNaoEncontrada() {

        when(tagRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> useCase.execute(999L)
                );

        assertEquals(
                "Tag com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(tagRepository).findById(999L);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(tagRepository, never()).save(any());
    }
}
