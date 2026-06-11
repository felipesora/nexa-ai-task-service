package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.workspace.WorkspaceRequestDTO;
import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.workspace.*;
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
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkspaceController.class)
class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarWorkspaceUseCase cadastrarWorkspaceUseCase;

    @MockitoBean
    private ListarTodosWorkspacesUseCase listarTodosWorkspacesUseCase;

    @MockitoBean
    private BuscarWorkspacePorIdUseCase buscarWorkspacePorIdUseCase;

    @MockitoBean
    private ListarWorkspacesPorIdUsuarioUseCase listarWorkspacesPorIdUsuarioUseCase;

    @MockitoBean
    private ListarWorkspacesPorIdUsuarioENomeUseCase listarWorkspacesPorIdUsuarioENomeUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private WorkspaceRequestDTO request;
    private WorkspaceResponseDTO response;

    @BeforeEach
    void setUp() {

        request = new WorkspaceRequestDTO(
                1L,
                "Meu Workspace",
                "Descrição",
                1L,
                1L
        );

        response = new WorkspaceResponseDTO(
                1L,
                1L,
                "Meu Workspace",
                "Descrição",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null,
                null
        );
    }

    @Test
    void deveCadastrarWorkspaceComSucesso() throws Exception {

        when(cadastrarWorkspaceUseCase.execute(request))
                .thenReturn(response);

        mockMvc.perform(post("/v1/workspaces")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/v1/workspaces/1"))
                .andExpect(jsonPath("$.id_workspace").value(1))
                .andExpect(jsonPath("$.nome").value("Meu Workspace"));
    }

    @Test
    void deveRetornar400QuandoNomeJaExistir() throws Exception {

        when(cadastrarWorkspaceUseCase.execute(request))
                .thenThrow(new BadRequestException(
                        "Já existe um workspace com esse nome para este usuário"
                ));

        mockMvc.perform(post("/v1/workspaces")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Já existe um workspace com esse nome para este usuário"));
    }

    @Test
    void deveRetornar404QuandoCorNaoForEncontrada() throws Exception {

        when(cadastrarWorkspaceUseCase.execute(request))
                .thenThrow(new EntityNotFoundException(
                        "Cor com id: 1 não encontrada"
                ));

        mockMvc.perform(post("/v1/workspaces")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cor com id: 1 não encontrada"));
    }

    @Test
    void deveRetornar404QuandoIconeNaoForEncontrado() throws Exception {

        when(cadastrarWorkspaceUseCase.execute(request))
                .thenThrow(new EntityNotFoundException(
                        "Ícone com id: 1 não encontrado"
                ));

        mockMvc.perform(post("/v1/workspaces")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Ícone com id: 1 não encontrado"));
    }

    @Test
    void deveListarTodosOsWorkspaces() throws Exception {

        Page<WorkspaceResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTodosWorkspacesUseCase.execute(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/workspaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_workspace").value(1))
                .andExpect(jsonPath("$.content[0].id_usuario").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Meu Workspace"))
                .andExpect(jsonPath("$.content[0].descricao").value("Descrição"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarWorkspacePorIdComSucesso() throws Exception {

        when(buscarWorkspacePorIdUseCase.execute(1L))
                .thenReturn(response);

        mockMvc.perform(get("/v1/workspaces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_workspace").value(1))
                .andExpect(jsonPath("$.id_usuario").value(1))
                .andExpect(jsonPath("$.nome").value("Meu Workspace"))
                .andExpect(jsonPath("$.descricao").value("Descrição"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveRetornar404QuandoWorkspaceNaoForEncontrado() throws Exception {

        when(buscarWorkspacePorIdUseCase.execute(999L))
                .thenThrow(new EntityNotFoundException(
                        "Workspace com id: 999 não encontrado."
                ));

        mockMvc.perform(get("/v1/workspaces/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Workspace com id: 999 não encontrado."));
    }

    @Test
    void deveListarWorkspacesPorIdUsuario() throws Exception {

        Page<WorkspaceResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarWorkspacesPorIdUsuarioUseCase
                .execute(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/workspaces/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_workspace").value(1))
                .andExpect(jsonPath("$.content[0].id_usuario").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Meu Workspace"))
                .andExpect(jsonPath("$.content[0].descricao").value("Descrição"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveListarWorkspacesPorIdUsuarioENome() throws Exception {

        Page<WorkspaceResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarWorkspacesPorIdUsuarioENomeUseCase
                .execute(eq(1L), eq("Meu Workspace"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(
                        get("/v1/workspaces/usuario/1")
                                .param("nome", "Meu Workspace")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_workspace").value(1))
                .andExpect(jsonPath("$.content[0].id_usuario").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Meu Workspace"))
                .andExpect(jsonPath("$.content[0].descricao").value("Descrição"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}