package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoverTagDaTarefaUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private RemoverTagDaTarefaUseCase useCase;

    private Tarefa tarefa;
    private Tag tag;

    @BeforeEach
    void setUp() {
        tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .build();
        tarefa.setTags(new ArrayList<>());

        tag = new TagBuilder()
                .comId(2L)
                .comIdUsuario(10L)
                .build();
    }

    @Test
    void deveRemoverTagDaTarefaComSucesso() {
        tarefa.getTags().add(tag);

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));
        when(tagRepository.findById(2L))
                .thenReturn(Optional.of(tag));

        useCase.execute(1L, 2L);

        assertTrue(tarefa.getTags().isEmpty());

        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {
        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> useCase.execute(1L, 2L));

        assertEquals(
                "Tarefa com id: 1 não encontrada.",
                exception.getMessage()
        );

        verify(tagRepository, never()).findById(anyLong());
        verify(tarefaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTagNaoEncontrada() {
        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));

        when(tagRepository.findById(2L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> useCase.execute(1L, 2L));

        assertEquals(
                "Tag com id: 2 não encontrada.",
                exception.getMessage()
        );

        verify(tarefaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTagPertenceAOutroUsuario() {
        tag.setIdUsuario(20L);

        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));
        when(tagRepository.findById(2L))
                .thenReturn(Optional.of(tag));

        BadRequestException exception =
                assertThrows(BadRequestException.class,
                        () -> useCase.execute(1L, 2L));

        assertEquals(
                "Esta tag não pertence ao mesmo usuário da tarefa.",
                exception.getMessage()
        );

        verify(tarefaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoTagNaoEstaVinculadaATarefa() {
        when(tarefaRepository.findById(1L))
                .thenReturn(Optional.of(tarefa));
        when(tagRepository.findById(2L))
                .thenReturn(Optional.of(tag));

        BadRequestException exception =
                assertThrows(BadRequestException.class,
                        () -> useCase.execute(1L, 2L));

        assertEquals(
                "A tag não está vinculada à tarefa.",
                exception.getMessage()
        );

        verify(tarefaRepository, never()).save(any());
    }
}