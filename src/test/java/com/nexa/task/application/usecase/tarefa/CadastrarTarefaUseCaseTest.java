package com.nexa.task.application.usecase.tarefa;

import com.nexa.task.application.dto.tarefa.TarefaCreateDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.mapper.TarefaControllerMapper;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.tarefa.DificuldadeTarefa;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.repository.TarefaRepository;
import com.nexa.task.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarTarefaUseCaseTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private TarefaControllerMapper mapper;

    @InjectMocks
    private CadastrarTarefaUseCase useCase;

    @Test
    void deveCadastrarTarefaComSucesso() {

        TarefaCreateDTO request = new TarefaCreateDTO(
                1L,
                "Minha tarefa",
                "Descrição da tarefa",
                PrioridadeTarefa.ALTA,
                DificuldadeTarefa.MEDIA,
                LocalDateTime.now().plusDays(1),
                1L
        );

        Workspace workspace = new WorkspaceBuilder()
                .comId(1L)
                .comNome("Workspace Teste")
                .build();

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comTitulo("Minha tarefa")
                .comDescricao("Descrição da tarefa")
                .build();

        TarefaResponseDTO response = new TarefaResponseDTO(
                1L,
                1L,
                "Minha tarefa",
                "Descrição da tarefa",
                PrioridadeTarefa.ALTA,
                StatusTarefa.PENDENTE,
                DificuldadeTarefa.MEDIA,
                request.dataLimite(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                1L
        );

        when(workspaceRepository.findById(1L))
                .thenReturn(Optional.of(workspace));

        when(mapper.toDomain(request, workspace))
                .thenReturn(tarefa);

        when(tarefaRepository.save(tarefa))
                .thenReturn(tarefa);

        when(mapper.toResponse(tarefa))
                .thenReturn(response);

        TarefaResponseDTO resultado = useCase.execute(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Minha tarefa", resultado.titulo());

        verify(workspaceRepository).findById(1L);
        verify(mapper).toDomain(request, workspace);
        verify(tarefaRepository).save(tarefa);
        verify(mapper).toResponse(tarefa);
    }

    @Test
    void deveLancarExcecaoQuandoWorkspaceNaoEncontrado() {

        TarefaCreateDTO request = new TarefaCreateDTO(
                1L,
                "Minha tarefa",
                "Descrição da tarefa",
                PrioridadeTarefa.ALTA,
                DificuldadeTarefa.MEDIA,
                LocalDateTime.now().plusDays(1),
                999L
        );

        when(workspaceRepository.findById(999L))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> useCase.execute(request)
        );

        assertEquals(
                "Workspace com id: 999 não encontrado",
                exception.getMessage()
        );

        verify(workspaceRepository).findById(999L);
        verifyNoInteractions(tarefaRepository);
        verifyNoInteractions(mapper);
    }
}