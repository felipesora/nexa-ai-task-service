package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.tarefa.TarefaCreateDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaUpdateDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.tarefa.*;
import com.nexa.task.domain.entity.tarefa.DificuldadeTarefa;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarTarefaUseCase cadastrarTarefaUseCase;

    @MockitoBean
    private ListarTodasTarefasUseCase listarTodasTarefasUseCase;

    @MockitoBean
    private BuscarTarefaPorIdUseCase buscarTarefaPorIdUseCase;

    @MockitoBean
    private ListarTarefasPorIdWorkspaceUseCase listarTarefasPorIdWorkspaceUseCase;

    @MockitoBean
    private ListarTarefasPorIdUsuarioUseCase listarTarefasPorIdUsuarioUseCase;

    @MockitoBean
    private ListarTarefasPorIdUsuarioETituloUseCase listarTarefasPorIdUsuarioETituloUseCase;

    @MockitoBean
    private AtualizarTarefaUseCase atualizarTarefaUseCase;

    @MockitoBean
    private ConcluirTarefaUseCase concluirTarefaUseCase;

    @MockitoBean
    private DesativarTarefaUseCase desativarTarefaUseCase;

    @MockitoBean
    private AtivarTarefaUseCase ativarTarefaUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private TarefaCreateDTO request;
    private TarefaResponseDTO response;
    private TarefaUpdateDTO updateDto;

    @BeforeEach
    void setUp() {

        LocalDateTime dataLimite = LocalDateTime.now().plusDays(1);

        request = new TarefaCreateDTO(
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

        updateDto = new TarefaUpdateDTO(
                "Minha tarefa atualizada",
                "Descrição atualizada",
                PrioridadeTarefa.MEDIA,
                DificuldadeTarefa.ALTA,
                LocalDateTime.now().plusDays(2)
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

        TarefaCreateDTO requestInvalido = new TarefaCreateDTO(
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

    @Test
    void deveListarTodasAsTarefas() throws Exception {

        Page<TarefaResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTodasTarefasUseCase.execute(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_tarefa").value(1))
                .andExpect(jsonPath("$.content[0].id_usuario").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Minha tarefa"))
                .andExpect(jsonPath("$.content[0].descricao").value("Descrição da tarefa"))
                .andExpect(jsonPath("$.content[0].prioridade").value("ALTA"))
                .andExpect(jsonPath("$.content[0].status").value("PENDENTE"))
                .andExpect(jsonPath("$.content[0].dificuldade").value("MEDIA"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.content[0].id_workspace").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarTarefaPorIdComSucesso() throws Exception {

        when(buscarTarefaPorIdUseCase.execute(1L))
                .thenReturn(response);

        mockMvc.perform(get("/v1/tarefas/1"))
                .andExpect(status().isOk())
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
    void deveRetornar404QuandoTarefaNaoForEncontrada() throws Exception {

        when(buscarTarefaPorIdUseCase.execute(999L))
                .thenThrow(
                        new EntityNotFoundException(
                                "Tarefa com id: 999 não encontrada."
                        )
                );

        mockMvc.perform(get("/v1/tarefas/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 999 não encontrada."));
    }

    @Test
    void deveListarTarefasPorIdWorkspace() throws Exception {

        Page<TarefaResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTarefasPorIdWorkspaceUseCase
                .execute(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/tarefas/workspace/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_tarefa").value(1))
                .andExpect(jsonPath("$.content[0].id_usuario").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Minha tarefa"))
                .andExpect(jsonPath("$.content[0].descricao").value("Descrição da tarefa"))
                .andExpect(jsonPath("$.content[0].prioridade").value("ALTA"))
                .andExpect(jsonPath("$.content[0].status").value("PENDENTE"))
                .andExpect(jsonPath("$.content[0].dificuldade").value("MEDIA"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.content[0].id_workspace").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveListarTarefasPorIdUsuario() throws Exception {

        Page<TarefaResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTarefasPorIdUsuarioUseCase
                .execute(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/tarefas/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_tarefa").value(1))
                .andExpect(jsonPath("$.content[0].id_usuario").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Minha tarefa"))
                .andExpect(jsonPath("$.content[0].descricao").value("Descrição da tarefa"))
                .andExpect(jsonPath("$.content[0].prioridade").value("ALTA"))
                .andExpect(jsonPath("$.content[0].status").value("PENDENTE"))
                .andExpect(jsonPath("$.content[0].dificuldade").value("MEDIA"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.content[0].id_workspace").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveListarTarefasPorIdUsuarioENome() throws Exception {

        Page<TarefaResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTarefasPorIdUsuarioETituloUseCase
                .execute(eq(1L), eq("Minha tarefa"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                        get("/v1/tarefas/usuario/1")
                                .param("titulo", "Minha tarefa")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_tarefa").value(1))
                .andExpect(jsonPath("$.content[0].id_usuario").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Minha tarefa"))
                .andExpect(jsonPath("$.content[0].descricao").value("Descrição da tarefa"))
                .andExpect(jsonPath("$.content[0].prioridade").value("ALTA"))
                .andExpect(jsonPath("$.content[0].status").value("PENDENTE"))
                .andExpect(jsonPath("$.content[0].dificuldade").value("MEDIA"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.content[0].id_workspace").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveAtualizarTarefaComSucesso() throws Exception {

        doNothing().when(atualizarTarefaUseCase)
                .execute(1L, updateDto);

        mockMvc.perform(
                        put("/v1/tarefas/1")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404QuandoTarefaNaoForEncontradaAoAtualizar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Tarefa com id: 999 não encontrada"
        ))
                .when(atualizarTarefaUseCase)
                .execute(eq(999L), any(TarefaUpdateDTO.class));

        mockMvc.perform(
                        put("/v1/tarefas/999")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 999 não encontrada"));
    }

    @Test
    void deveRetornar400QuandoRequestDeAtualizacaoForInvalido() throws Exception {

        TarefaUpdateDTO dtoInvalido = new TarefaUpdateDTO(
                "",
                "",
                null,
                null,
                null
        );

        mockMvc.perform(
                        put("/v1/tarefas/1")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dtoInvalido))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Erro de validação"));
    }

    @Test
    void deveConcluirTarefaComSucesso() throws Exception {

        doNothing().when(concluirTarefaUseCase)
                .execute(1L);

        mockMvc.perform(
                        patch("/v1/tarefas/1/concluir")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404QuandoTarefaNaoForEncontradaAoConcluir() throws Exception {

        doThrow(new EntityNotFoundException(
                "Tarefa com id: 999 não encontrada"
        ))
                .when(concluirTarefaUseCase)
                .execute(999L);

        mockMvc.perform(
                        patch("/v1/tarefas/999/concluir")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 999 não encontrada"));
    }

    @Test
    void deveRetornar400QuandoTarefaJaEstiverConcluida() throws Exception {

        doThrow(new BadRequestException(
                "A tarefa já está concluída."
        ))
                .when(concluirTarefaUseCase)
                .execute(1L);

        mockMvc.perform(
                        patch("/v1/tarefas/1/concluir")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("A tarefa já está concluída."));
    }

    @Test
    void deveDesativarTarefaComSucesso() throws Exception {

        doNothing().when(desativarTarefaUseCase)
                .execute(1L);

        mockMvc.perform(delete("/v1/tarefas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404QuandoTarefaNaoForEncontradaAoDesativar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Tarefa com id: 999 não encontrada"
        ))
                .when(desativarTarefaUseCase)
                .execute(999L);

        mockMvc.perform(delete("/v1/tarefas/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 999 não encontrada"));
    }

    @Test
    void deveAtivarTarefaComSucesso() throws Exception {

        doNothing().when(ativarTarefaUseCase)
                .execute(1L);

        mockMvc.perform(patch("/v1/tarefas/ativar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404QuandoTarefaNaoForEncontradaAoAtivar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Tarefa com id: 999 não encontrada"
        ))
                .when(ativarTarefaUseCase)
                .execute(999L);

        mockMvc.perform(patch("/v1/tarefas/ativar/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 999 não encontrada"));
    }
}
