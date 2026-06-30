package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarTagsPorIdTarefaUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TagControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private ListarTagsPorIdTarefaUseCase useCase;

    @Test
    void deveListarTagsPorIdTarefaComPaginacao() {

        Long idTarefa = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Tarefa tarefa = new TarefaBuilder()
                .comId(idTarefa)
                .comIdUsuario(10L)
                .build();

        Tag tag1 = new TagBuilder()
                .comId(1L)
                .comNome("Urgente")
                .build();

        Tag tag2 = new TagBuilder()
                .comId(2L)
                .comNome("Backend")
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
                "Backend",
                true,
                null
        );

        when(tarefaRepository.findById(idTarefa))
                .thenReturn(Optional.of(tarefa));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        when(tagRepository.findByIdTarefa(idTarefa, pageable))
                .thenReturn(pageEntity);

        when(mapper.toResponse(tag1))
                .thenReturn(response1);

        when(mapper.toResponse(tag2))
                .thenReturn(response2);

        Page<TagResponseDTO> resultado = useCase.execute(idTarefa, pageable);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals(2, resultado.getContent().size());
        assertEquals("Urgente", resultado.getContent().get(0).nome());
        assertEquals("Backend", resultado.getContent().get(1).nome());

        verify(tarefaRepository).findById(idTarefa);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(tagRepository).findByIdTarefa(idTarefa, pageable);
        verify(mapper).toResponse(tag1);
        verify(mapper).toResponse(tag2);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoForEncontrada() {

        Long idTarefa = 999L;
        Pageable pageable = PageRequest.of(0, 10);

        when(tarefaRepository.findById(idTarefa))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(idTarefa, pageable)
        );

        assertEquals(
                "Tarefa com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(tarefaRepository).findById(idTarefa);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(tagRepository, never()).findByIdTarefa(any(), any());
    }
}
