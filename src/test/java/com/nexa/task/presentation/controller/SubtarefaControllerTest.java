package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.subtarefa.SubtarefaRequestDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.subtarefa.CadastrarSubtarefaUseCase;
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

@WebMvcTest(SubtarefaController.class)
class SubtarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CadastrarSubtarefaUseCase cadastrarSubtarefaUseCase;

    private SubtarefaRequestDTO request;
    private SubtarefaResponseDTO response;

    @BeforeEach
    void setUp() {

        request = new SubtarefaRequestDTO(
                "Minha subtarefa",
                1L
        );

        response = new SubtarefaResponseDTO(
                1L,
                "Minha subtarefa",
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                1L
        );
    }

    @Test
    void deveCadastrarSubtarefaComSucesso() throws Exception {

        when(cadastrarSubtarefaUseCase.execute(request))
                .thenReturn(response);

        mockMvc.perform(post("/v1/subtarefas")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/v1/subtarefas/1"))
                .andExpect(jsonPath("$.id_subtarefa").value(1))
                .andExpect(jsonPath("$.titulo")
                        .value("Minha subtarefa"))
                .andExpect(jsonPath("$.concluida")
                        .value(false))
                .andExpect(jsonPath("$.ativo")
                        .value(true))
                .andExpect(jsonPath("$.id_tarefa")
                        .value(1));
    }

    @Test
    void deveRetornar404QuandoTarefaNaoForEncontrada() throws Exception {

        when(cadastrarSubtarefaUseCase.execute(request))
                .thenThrow(
                        new EntityNotFoundException(
                                "Tarefa com id: 1 não encontrada"
                        )
                );

        mockMvc.perform(post("/v1/subtarefas")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 1 não encontrada"));
    }

    @Test
    void deveRetornar400QuandoRequestForInvalido() throws Exception {

        SubtarefaRequestDTO requestInvalido =
                new SubtarefaRequestDTO(
                        "",
                        null
                );

        mockMvc.perform(post("/v1/subtarefas")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Erro de validação"));
    }
}