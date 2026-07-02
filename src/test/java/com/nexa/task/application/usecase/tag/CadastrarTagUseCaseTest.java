package com.nexa.task.application.usecase.tag;

import com.nexa.task.application.dto.tag.TagCreateDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TagControllerMapper;
import com.nexa.task.domain.builder.tag.CorTagBuilder;
import com.nexa.task.domain.builder.tag.TagBuilder;
import com.nexa.task.domain.entity.tag.CorTag;
import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.repository.CorTagRepository;
import com.nexa.task.domain.repository.TagRepository;
import com.nexa.task.infra.security.AuthenticatedUser;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarTagUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private CorTagRepository corTagRepository;

    @Mock
    private TagControllerMapper mapper;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private CadastrarTagUseCase useCase;

    @Test
    void deveCadastrarTagComSucessoSemCor() {

        TagCreateDTO request = new TagCreateDTO(
                "Urgente",
                null
        );

        AuthenticatedUser user =
                new AuthenticatedUser(
                        1L,
                        "admin@email.com",
                        "ADMIN"
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

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(tagRepository.existsByNomeAndIdUsuario("Urgente", 1L)).thenReturn(false);
        when(mapper.toDomain(request, null, 1L)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(mapper.toResponse(tag)).thenReturn(response);

        TagResponseDTO resultado = useCase.execute(request);

        assertNotNull(resultado);
        assertEquals(response, resultado);

        verify(authService).getAuthenticatedUser();
        verify(tagRepository).existsByNomeAndIdUsuario("Urgente", 1L);
        verify(mapper).toDomain(request, null, 1L);
        verify(tagRepository).save(tag);
        verify(mapper).toResponse(tag);
    }

    @Test
    void deveCadastrarTagComSucessoComCor() {

        TagCreateDTO request = new TagCreateDTO(
                "Estudo",
                10L
        );

        AuthenticatedUser user =
                new AuthenticatedUser(
                        1L,
                        "admin@email.com",
                        "ADMIN"
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

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(tagRepository.existsByNomeAndIdUsuario("Estudo", 1L)).thenReturn(false);
        when(corTagRepository.findByIdAtivo(10L)).thenReturn(Optional.of(corTag));
        when(mapper.toDomain(request, corTag, 1L)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(mapper.toResponse(tag)).thenReturn(response);

        TagResponseDTO resultado = useCase.execute(request);

        assertNotNull(resultado);
        assertEquals(response, resultado);

        verify(authService).getAuthenticatedUser();
        verify(tagRepository).existsByNomeAndIdUsuario("Estudo", 1L);
        verify(corTagRepository).findByIdAtivo(10L);
        verify(mapper).toDomain(request, corTag, 1L);
        verify(tagRepository).save(tag);
        verify(mapper).toResponse(tag);
    }

    @Test
    void deveLancarExcecaoQuandoCorNaoForEncontrada() {

        TagCreateDTO request = new TagCreateDTO(
                "Importante",
                99L
        );

        AuthenticatedUser user =
                new AuthenticatedUser(
                        1L,
                        "admin@email.com",
                        "ADMIN"
                );

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(tagRepository.existsByNomeAndIdUsuario("Importante", 1L)).thenReturn(false);
        when(corTagRepository.findByIdAtivo(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(request)
        );

        assertEquals("Cor com id: 99 não encontrada", exception.getMessage());

        verify(tagRepository, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExistirParaUsuario() {

        TagCreateDTO request = new TagCreateDTO(
                "Urgente",
                10L
        );

        AuthenticatedUser user =
                new AuthenticatedUser(
                        1L,
                        "admin@email.com",
                        "ADMIN"
                );

        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(tagRepository.existsByNomeAndIdUsuario("Urgente", 1L))
                .thenReturn(true);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> useCase.execute(request)
        );

        assertEquals(
                "Já existe uma tag com esse nome para este usuário",
                exception.getMessage()
        );

        verify(corTagRepository, never()).findByIdAtivo(anyLong());
        verify(tagRepository, never()).save(any());
        verify(mapper, never()).toDomain(any(), any(), anyLong());
    }
}
