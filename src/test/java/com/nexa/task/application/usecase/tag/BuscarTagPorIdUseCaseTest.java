package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TagControllerMapper;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarTagPorIdUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private BuscarTagPorIdUseCase useCase;

    @Test
    void deveBuscarTagPorIdComSucesso() {

        Long id = 1L;

        Tag tag = new TagBuilder()
                .comId(id)
                .comIdUsuario(1L)
                .comNome("Urgente")
                .comAtivo(true)
                .build();

        TagResponseDTO response = new TagResponseDTO(
                id,
                1L,
                "Urgente",
                true,
                null
        );

        when(tagRepository.findByIdAtivo(id))
                .thenReturn(Optional.of(tag));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        when(mapper.toResponse(tag))
                .thenReturn(response);

        TagResponseDTO resultado = useCase.execute(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals("Urgente", resultado.nome());

        verify(tagRepository).findByIdAtivo(id);
        verify(authService).validateOwnerOrAdmin(1L);
        verify(mapper).toResponse(tag);
    }

    @Test
    void deveLancarExcecaoQuandoTagNaoForEncontrada() {

        Long id = 999L;

        when(tagRepository.findByIdAtivo(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(id)
        );

        assertEquals(
                "Tag com id: 999 não encontrada.",
                exception.getMessage()
        );

        verify(tagRepository).findByIdAtivo(id);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(mapper, never()).toResponse(any());
    }
}
