package com.nexa.task.application.usecase.corTag;

import com.nexa.task.application.exception.EntityNotFoundException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtivarCorTagUseCaseTest {

    @Mock
    private CorTagRepository repository;

    @InjectMocks
    private AtivarCorTagUseCase useCase;

    @Test
    void deveAtivarCorTagComSucesso() {

        CorTag cor = new CorTagBuilder()
                .comAtivo(false)
                .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(cor));

        useCase.execute(1L);

        assertTrue(cor.getAtivo());

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
    }
}