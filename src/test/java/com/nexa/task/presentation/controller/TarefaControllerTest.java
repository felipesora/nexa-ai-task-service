package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaCreateDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaUpdateDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.subtarefa.ListarSubtarefasPorIdTarefaUseCase;
import com.nexa.task.application.usecase.tag.ListarTagsPorIdTarefaUseCase;
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
    private ListarTarefasPorIdUsuarioUseCase listarTarefasPorIdUsuarioUseCase;

    @MockitoBean
    private ListarTarefasPorIdUsuarioETituloUseCase listarTarefasPorIdUsuarioETituloUseCase;

    @MockitoBean
    private ListarSubtarefasPorIdTarefaUseCase listarSubtarefasPorIdTarefaUseCase;

    @MockitoBean
    private ListarTagsPorIdTarefaUseCase listarTagsPorIdTarefaUseCase;

    @MockitoBean
    private AtualizarTarefaUseCase atualizarTarefaUseCase;

    @MockitoBean
    private ConcluirTarefaUseCase concluirTarefaUseCase;

    @MockitoBean
    private IniciarTarefaUseCase iniciarTarefaUseCase;

    @MockitoBean
    private ReabrirTarefaUseCase reabrirTarefaUseCase;

    @MockitoBean
    private DesativarTarefaUseCase desativarTarefaUseCase;

    @MockitoBean
    private AtivarTarefaUseCase ativarTarefaUseCase;

    @MockitoBean
    private AdicionarTagNaTarefaUseCase adicionarTagNaTarefaUseCase;

    @MockitoBean
    private RemoverTagDaTarefaUseCase removerTagDaTarefaUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private TarefaCreateDTO request;
    private TarefaResponseDTO response;
    private TarefaUpdateDTO updateDto;
    private SubtarefaResponseDTO subtarefaResponse;
    private TagResponseDTO tagResponse;

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

        subtarefaResponse = new SubtarefaResponseDTO(
                1L,
                "Minha subtarefa",
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                1L
        );

        tagResponse = new TagResponseDTO(
                1L,
                1L,
                "Backend",
                true,
                null
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
    void deveListarSubtarefasPelaTarefa() throws Exception {

        Page<SubtarefaResponseDTO> page =
                new PageImpl<>(List.of(subtarefaResponse));

        when(listarSubtarefasPorIdTarefaUseCase
                .execute(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/tarefas/1/subtarefas"))
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
    void deveRetornar404QuandoTarefaNaoForEncontradaAoListarSubtarefas()
            throws Exception {

        when(listarSubtarefasPorIdTarefaUseCase
                .execute(eq(999L), any(Pageable.class)))
                .thenThrow(
                        new EntityNotFoundException(
                                "Tarefa com id: 999 não encontrada."
                        )
                );

        mockMvc.perform(get("/v1/tarefas/999/subtarefas"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 999 não encontrada."));
    }

    @Test
    void deveListarTagsPelaTarefa() throws Exception {

        Page<TagResponseDTO> page =
                new PageImpl<>(List.of(tagResponse));

        when(listarTagsPorIdTarefaUseCase
                .execute(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/tarefas/1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_tag")
                        .value(1))
                .andExpect(jsonPath("$.content[0].nome")
                        .value("Backend"))
                .andExpect(jsonPath("$.content[0].ativo")
                        .value(true))
                .andExpect(jsonPath("$.content[0].id_usuario")
                        .value(1))
                .andExpect(jsonPath("$.totalElements")
                        .value(1));
    }

    @Test
    void deveRetornar404QuandoTarefaNaoForEncontradaAoListarTags()
            throws Exception {

        when(listarTagsPorIdTarefaUseCase
                .execute(eq(999L), any(Pageable.class)))
                .thenThrow(
                        new EntityNotFoundException(
                                "Tarefa com id: 999 não encontrada."
                        )
                );

        mockMvc.perform(get("/v1/tarefas/999/tags"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 999 não encontrada."));
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
    void deveIniciarTarefaComSucesso() throws Exception {

        doNothing().when(iniciarTarefaUseCase)
                .execute(1L);

        mockMvc.perform(
                        patch("/v1/tarefas/1/iniciar")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404QuandoTarefaNaoForEncontradaAoIniciar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Tarefa com id: 999 não encontrada"
        ))
                .when(iniciarTarefaUseCase)
                .execute(999L);

        mockMvc.perform(
                        patch("/v1/tarefas/999/iniciar")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 999 não encontrada"));
    }

    @Test
    void deveRetornar400QuandoTarefaJaEstiverEmAndamento() throws Exception {

        doThrow(new BadRequestException(
                "A tarefa já está em andamento."
        ))
                .when(iniciarTarefaUseCase)
                .execute(1L);

        mockMvc.perform(
                        patch("/v1/tarefas/1/iniciar")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("A tarefa já está em andamento."));
    }

    @Test
    void deveRetornar400QuandoTarefaEstiverConcluidaAoIniciar() throws Exception {

        doThrow(new BadRequestException(
                "Tarefas concluídas devem ser reabertas antes de serem iniciadas."
        ))
                .when(iniciarTarefaUseCase)
                .execute(1L);

        mockMvc.perform(
                        patch("/v1/tarefas/1/iniciar")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Tarefas concluídas devem ser reabertas antes de serem iniciadas."));
    }

    @Test
    void deveReabrirTarefaComSucesso() throws Exception {

        doNothing().when(reabrirTarefaUseCase)
                .execute(1L);

        mockMvc.perform(
                        patch("/v1/tarefas/1/reabrir")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404QuandoTarefaNaoForEncontradaAoReabrir() throws Exception {

        doThrow(new EntityNotFoundException(
                "Tarefa com id: 999 não encontrada"
        ))
                .when(reabrirTarefaUseCase)
                .execute(999L);

        mockMvc.perform(
                        patch("/v1/tarefas/999/reabrir")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 999 não encontrada"));
    }

    @Test
    void deveRetornar400QuandoTarefaNaoPuderSerReaberta() throws Exception {

        doThrow(new BadRequestException(
                "Somente tarefas concluídas podem ser reabertas."
        ))
                .when(reabrirTarefaUseCase)
                .execute(1L);

        mockMvc.perform(
                        patch("/v1/tarefas/1/reabrir")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Somente tarefas concluídas podem ser reabertas."));
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

    @Test
    void deveAdicionarTagNaTarefaComSucesso() throws Exception {

        doNothing()
                .when(adicionarTagNaTarefaUseCase)
                .execute(1L, 2L);

        mockMvc.perform(post("/v1/tarefas/1/tags/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404QuandoAdicionarTagETarefaNaoForEncontrada()
            throws Exception {

        doThrow(new EntityNotFoundException(
                "Tarefa com id: 1 não encontrada."
        ))
                .when(adicionarTagNaTarefaUseCase)
                .execute(1L, 2L);

        mockMvc.perform(post("/v1/tarefas/1/tags/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 1 não encontrada."));
    }

    @Test
    void deveRetornar400QuandoTagJaEstiverVinculada()
            throws Exception {

        doThrow(new BadRequestException(
                "A tag já está vinculada à tarefa."
        ))
                .when(adicionarTagNaTarefaUseCase)
                .execute(1L, 2L);

        mockMvc.perform(post("/v1/tarefas/1/tags/2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("A tag já está vinculada à tarefa."));
    }

    @Test
    void deveRetornar404QuandoRemoverTagETarefaNaoForEncontrada()
            throws Exception {

        doThrow(new EntityNotFoundException(
                "Tarefa com id: 1 não encontrada."
        ))
                .when(removerTagDaTarefaUseCase)
                .execute(1L, 2L);

        mockMvc.perform(delete("/v1/tarefas/1/tags/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tarefa com id: 1 não encontrada."));
    }

    @Test
    void deveRetornar400QuandoTagNaoEstiverVinculada()
            throws Exception {

        doThrow(new BadRequestException(
                "A tag não está vinculada à tarefa."
        ))
                .when(removerTagDaTarefaUseCase)
                .execute(1L, 2L);

        mockMvc.perform(delete("/v1/tarefas/1/tags/2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("A tag não está vinculada à tarefa."));
    }
}
