package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.tag.CorTagBuilder;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DesativarCorTagUseCaseTest {

    @Mock
    private CorTagRepository repository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private DesativarCorTagUseCase useCase;

    @Test
    void deveDesativarCorTagComSucesso() {

        CorTag cor = new CorTagBuilder()
                .comAtivo(true)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(cor));

        useCase.execute(1L);

        assertFalse(cor.getAtivo());

        verify(tagRepository).removerCorDasTags(1L);
        verify(repository).save(cor);
    }

    @Test
    void deveLancarExcecaoQuandoCorTagNaoExistir() {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(999L)
        );

        verify(tagRepository, never()).removerCorDasTags(999L);
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any(CorTag.class));
    }
}