package com.nexa.task.application.mapper;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.domain.builder.tarefa.TarefaBuilder;
import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.repository.SubtarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarefaControllerMapperTest {

    @Mock
    private SubtarefaRepository subtarefaRepository;

    private TarefaControllerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TarefaControllerMapper(subtarefaRepository);
    }

    @Test
    void deveIncluirQuantidadeDeSubtarefasAtivasNoResponse() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(1L)
                .comTitulo("Minha tarefa")
                .comWorkspace(new WorkspaceBuilder().comId(10L).build())
                .build();

        when(subtarefaRepository.countAtivasByIdTarefa(1L)).thenReturn(3L);

        TarefaResponseDTO response = mapper.toResponse(tarefa);

        assertEquals(1L, response.id());
        assertEquals("Minha tarefa", response.titulo());
        assertEquals(10L, response.idWorkspace());
        assertEquals(3L, response.quantidadeSubtarefas());
        verify(subtarefaRepository).countAtivasByIdTarefa(1L);
    }

    @Test
    void deveRetornarZeroQuandoTarefaNaoTemSubtarefas() {

        Tarefa tarefa = new TarefaBuilder()
                .comId(2L)
                .comWorkspace(new WorkspaceBuilder().comId(10L).build())
                .build();

        when(subtarefaRepository.countAtivasByIdTarefa(2L)).thenReturn(0L);

        TarefaResponseDTO response = mapper.toResponse(tarefa);

        assertEquals(0L, response.quantidadeSubtarefas());
    }
}
