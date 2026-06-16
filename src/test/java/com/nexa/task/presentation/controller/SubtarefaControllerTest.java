package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.subtarefa.SubtarefaRequestDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.subtarefa.BuscarSubtarefaPorIdUseCase;
import com.nexa.task.application.usecase.subtarefa.CadastrarSubtarefaUseCase;
import com.nexa.task.application.usecase.subtarefa.ListarSubtarefasPorIdTarefaUseCase;
import com.nexa.task.application.usecase.subtarefa.ListarTodasSubtarefasUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockitoBean
    private ListarTodasSubtarefasUseCase listarTodasSubtarefasUseCase;

    @MockitoBean
    private ListarSubtarefasPorIdTarefaUseCase listarSubtarefasPorIdTarefaUseCase;

    @MockitoBean
    private BuscarSubtarefaPorIdUseCase buscarSubtarefaPorIdUseCase;

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

    @Test
    void deveListarTodasAsSubtarefas() throws Exception {

        Page<SubtarefaResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTodasSubtarefasUseCase.execute(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/subtarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_subtarefa").value(1))
                .andExpect(jsonPath("$.content[0].titulo")
                        .value("Minha subtarefa"))
                .andExpect(jsonPath("$.content[0].concluida")
                        .value(false))
                .andExpect(jsonPath("$.content[0].ativo")
                        .value(true))
                .andExpect(jsonPath("$.content[0].id_tarefa")
                        .value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarSubtarefaPorIdComSucesso() throws Exception {

        when(buscarSubtarefaPorIdUseCase.execute(1L))
                .thenReturn(response);

        mockMvc.perform(get("/v1/subtarefas/1"))
                .andExpect(status().isOk())
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
    void deveRetornar404QuandoSubtarefaNaoForEncontrada() throws Exception {

        when(buscarSubtarefaPorIdUseCase.execute(999L))
                .thenThrow(
                        new EntityNotFoundException(
                                "Subtarefa com id: 999 não encontrada."
                        )
                );

        mockMvc.perform(get("/v1/subtarefas/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Subtarefa com id: 999 não encontrada."));
    }

    @Test
    void deveListarSubtarefasPorIdTarefa() throws Exception {

        Page<SubtarefaResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarSubtarefasPorIdTarefaUseCase
                .execute(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/subtarefas/tarefa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_subtarefa").value(1))
                .andExpect(jsonPath("$.content[0].titulo")
                        .value("Minha subtarefa"))
                .andExpect(jsonPath("$.content[0].concluida")
                        .value(false))
                .andExpect(jsonPath("$.content[0].ativo")
                        .value(true))
                .andExpect(jsonPath("$.content[0].id_tarefa")
                        .value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }


}