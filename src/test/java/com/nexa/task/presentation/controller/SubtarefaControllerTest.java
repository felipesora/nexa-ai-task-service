package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.subtarefa.SubtarefaCreateDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.subtarefa.*;
import com.nexa.task.application.dto.subtarefa.SubtarefaUpdateDTO;
import com.nexa.task.infra.security.JwtAuthenticationFilter;
import com.nexa.task.infra.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubtarefaController.class)
@AutoConfigureMockMvc(addFilters = false)
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
    private BuscarSubtarefaPorIdUseCase buscarSubtarefaPorIdUseCase;

    @MockitoBean
    private AtualizarSubtarefaUseCase atualizarSubtarefaUseCase;

    @MockitoBean
    private DesativarSubtarefaUseCase desativarSubtarefaUseCase;

    @MockitoBean
    private AtivarSubtarefaUseCase ativarSubtarefaUseCase;

    @MockitoBean
    private ConcluirSubtarefaUseCase concluirSubtarefaUseCase;

    @MockitoBean
    private DesmarcarSubtarefaConcluidaUseCase desmarcarSubtarefaConcluidaUseCase;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private SubtarefaCreateDTO request;
    private SubtarefaResponseDTO response;
    private SubtarefaUpdateDTO updateDto;

    @BeforeEach
    void setUp() {

        request = new SubtarefaCreateDTO(
                "Minha subtarefa",
                1L
        );

        response = new SubtarefaResponseDTO(
                1L,
                1L,
                "Minha subtarefa",
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                1L
        );

        updateDto = new SubtarefaUpdateDTO(
                "Subtarefa atualizada"
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    void deveRetornar400QuandoRequestForInvalido() throws Exception {

        SubtarefaCreateDTO requestInvalido =
                new SubtarefaCreateDTO(
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarSubtarefaComSucesso() throws Exception {

        doNothing().when(atualizarSubtarefaUseCase)
                .execute(1L, updateDto);

        mockMvc.perform(
                        put("/v1/subtarefas/1")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoSubtarefaNaoForEncontradaAoAtualizar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Subtarefa com id: 999 não encontrada."
        ))
                .when(atualizarSubtarefaUseCase)
                .execute(eq(999L), any(SubtarefaUpdateDTO.class));

        mockMvc.perform(
                        put("/v1/subtarefas/999")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Subtarefa com id: 999 não encontrada."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar400QuandoRequestDeAtualizacaoForInvalido() throws Exception {

        SubtarefaUpdateDTO dtoInvalido =
                new SubtarefaUpdateDTO("");

        mockMvc.perform(
                        put("/v1/subtarefas/1")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoInvalido))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Erro de validação"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDesativarSubtarefaComSucesso() throws Exception {

        doNothing().when(desativarSubtarefaUseCase)
                .execute(1L);

        mockMvc.perform(delete("/v1/subtarefas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoSubtarefaNaoForEncontradaAoDesativar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Subtarefa com id: 999 não encontrada"
        ))
                .when(desativarSubtarefaUseCase)
                .execute(999L);

        mockMvc.perform(delete("/v1/subtarefas/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Subtarefa com id: 999 não encontrada"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtivarSubtarefaComSucesso() throws Exception {

        doNothing().when(ativarSubtarefaUseCase)
                .execute(1L);

        mockMvc.perform(patch("/v1/subtarefas/ativar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoSubtarefaNaoForEncontradaAoAtivar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Subtarefa com id: 999 não encontrada"
        ))
                .when(ativarSubtarefaUseCase)
                .execute(999L);

        mockMvc.perform(patch("/v1/subtarefas/ativar/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Subtarefa com id: 999 não encontrada"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveConcluirSubtarefaComSucesso() throws Exception {

        doNothing().when(concluirSubtarefaUseCase)
                .execute(1L);

        mockMvc.perform(
                        patch("/v1/subtarefas/1/concluir")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoSubtarefaNaoForEncontradaAoConcluir() throws Exception {

        doThrow(new EntityNotFoundException(
                "Subtarefa com id: 999 não encontrada"
        ))
                .when(concluirSubtarefaUseCase)
                .execute(999L);

        mockMvc.perform(
                        patch("/v1/subtarefas/999/concluir")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Subtarefa com id: 999 não encontrada"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDesmarcarConclusaoSubtarefaComSucesso() throws Exception {

        doNothing().when(desmarcarSubtarefaConcluidaUseCase)
                .execute(1L);

        mockMvc.perform(
                        patch("/v1/subtarefas/1/desmarcar-conclusao")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoSubtarefaNaoForEncontradaAoDesmarcarConclusao() throws Exception {

        doThrow(new EntityNotFoundException(
                "Subtarefa com id: 999 não encontrada"
        ))
                .when(desmarcarSubtarefaConcluidaUseCase)
                .execute(999L);

        mockMvc.perform(
                        patch("/v1/subtarefas/999/desmarcar-conclusao")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Subtarefa com id: 999 não encontrada"));
    }
}
