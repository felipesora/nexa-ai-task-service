package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaUpdateDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.entity.tarefa.DificuldadeTarefa;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.infra.security.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarTarefaUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private AtualizarTarefaUseCase useCase;

    @Test
    void deveAtualizarTarefaComSucesso() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comIdUsuario(10L)
                .comTitulo("Título antigo")
                .comDescricao("Descrição antiga")
                .build();

        LocalDateTime dataLimite = LocalDateTime.now().plusDays(5);

        TarefaUpdateDTO dto = new TarefaUpdateDTO(
                "Novo título",
                "Nova descrição",
                PrioridadeTarefa.ALTA,
                DificuldadeTarefa.MEDIA,
                dataLimite
        );

        when(tarefaRepository.findByIdAtivo(1L))
                .thenReturn(Optional.of(tarefa));

        doNothing().when(authService).validateOwnerOrAdmin(anyLong());

        useCase.execute(1L, dto);

        assertEquals("Novo título", tarefa.getTitulo());
        assertEquals("Nova descrição", tarefa.getDescricao());
        assertEquals(PrioridadeTarefa.ALTA, tarefa.getPrioridade());
        assertEquals(DificuldadeTarefa.MEDIA, tarefa.getDificuldade());
        assertEquals(dataLimite, tarefa.getDataLimite());
        assertNotNull(tarefa.getAtualizadoEm());

        verify(tarefaRepository).findByIdAtivo(1L);
        verify(authService).validateOwnerOrAdmin(10L);
        verify(tarefaRepository).save(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoForEncontrada() {

        when(tarefaRepository.findByIdAtivo(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> useCase.execute(
                                999L,
                                mock(TarefaUpdateDTO.class)
                        )
                );

        assertEquals(
                "Tarefa com id: 999 não encontrada",
                exception.getMessage()
        );

        verify(tarefaRepository).findByIdAtivo(999L);
        verify(authService, never()).validateOwnerOrAdmin(anyLong());
        verify(tarefaRepository, never()).save(any());
    }
}