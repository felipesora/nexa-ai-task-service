package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.tarefa.TarefaRequestDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.tarefa.CadastrarTarefaUseCase;
import com.nexa.task.domain.entity.tarefa.DificuldadeTarefa;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarTarefaUseCase cadastrarTarefaUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private TarefaRequestDTO request;
    private TarefaResponseDTO response;

    @BeforeEach
    void setUp() {

        LocalDateTime dataLimite = LocalDateTime.now().plusDays(1);

        request = new TarefaRequestDTO(
                1L,
                "Minha tarefa",
                "Descrição da tarefa",
                PrioridadeTarefa.ALTA,
                DificuldadeTarefa.MEDIA,
                dataLimite,
                1L
        );

        response = new TarefaResponseDTO(
                1L,
                1L,
                "Minha tarefa",
                "Descrição da tarefa",
                PrioridadeTarefa.ALTA,
                StatusTarefa.PENDENTE,
                DificuldadeTarefa.MEDIA,
                dataLimite,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                1L
        );
    }

    @Test
    void deveCadastrarTarefaComSucesso() throws Exception {

        when(cadastrarTarefaUseCase.execute(request))
                .thenReturn(response);

        mockMvc.perform(post("/v1/tarefas")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/v1/tarefas/1"))
                .andExpect(jsonPath("$.id_tarefa").value(1))
                .andExpect(jsonPath("$.id_usuario").value(1))
                .andExpect(jsonPath("$.titulo").value("Minha tarefa"))
                .andExpect(jsonPath("$.descricao").value("Descrição da tarefa"))
                .andExpect(jsonPath("$.prioridade").value("ALTA"))
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.dificuldade").value("MEDIA"))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.id_workspace").value(1));
    }

    @Test
    void deveRetornar404QuandoWorkspaceNaoForEncontrado() throws Exception {

        when(cadastrarTarefaUseCase.execute(request))
                .thenThrow(new EntityNotFoundException(
                        "Workspace com id: 1 não encontrado"
                ));

        mockMvc.perform(post("/v1/tarefas")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Workspace com id: 1 não encontrado"));
    }

    @Test
    void deveRetornar400QuandoRequestForInvalido() throws Exception {

        TarefaRequestDTO requestInvalido = new TarefaRequestDTO(
                null,
                "",
                "",
                null,
                null,
                null,
                null
        );

        mockMvc.perform(post("/v1/tarefas")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Erro de validação"));
    }
}