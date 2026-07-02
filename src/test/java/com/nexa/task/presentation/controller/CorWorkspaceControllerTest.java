package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.corWorkspace.CorWorkspaceRequestDTO;
import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.corWorkspace.*;
import com.nexa.task.domain.builder.workspace.CorWorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.CorWorkspace;
import com.nexa.task.infra.security.JwtAuthenticationFilter;
import com.nexa.task.infra.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CorWorkspaceController.class)
@AutoConfigureMockMvc(addFilters = false)
class CorWorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarCorWorkspaceUseCase cadastrarCorWorkspaceUseCase;

    @MockitoBean
    private ListarTodasCoresWorkspaceUseCase listarTodasCoresWorkspaceUseCase;

    @MockitoBean
    private BuscarCorWorkspacePorIdUserCase buscarCorWorkspacePorIdUserCase;

    @MockitoBean
    private DesativarCorWorkspaceUseCase desativarCorWorkspaceUseCase;

    @MockitoBean
    private AtivarCorWorkspaceUseCase ativarCorWorkspaceUseCase;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private CorWorkspaceRequestDTO request;
    private CorWorkspace cor;
    private CorWorkspaceResponseDTO response;

    @BeforeEach
    void setUp() {

        request = new CorWorkspaceRequestDTO(
                "#0000FF"
        );

        cor = new CorWorkspaceBuilder()
                .comId(1L)
                .comCor("#0000FF")
                .comAtivo(true)
                .build();

        response = new CorWorkspaceResponseDTO(
                cor.getId(),
                cor.getCor(),
                cor.getAtivo()
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCadastrarCorComSucesso() throws Exception {

        when(cadastrarCorWorkspaceUseCase.execute(request))
                .thenReturn(response);

        mockMvc.perform(post("/v1/cores-workspace")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/v1/cores-workspace/1"))
                .andExpect(jsonPath("$.id_cor").value(1))
                .andExpect(jsonPath("$.cor").value("#0000FF"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar400QuandoCorJaExistir() throws Exception {

        when(cadastrarCorWorkspaceUseCase.execute(request))
                .thenThrow(new BadRequestException(
                        "Esta cor já está cadastrada."
                ));

        mockMvc.perform(post("/v1/cores-workspace")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Esta cor já está cadastrada."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar400QuandoCorForInvalida() throws Exception {

        CorWorkspaceRequestDTO requestInvalido =
                new CorWorkspaceRequestDTO("");

        mockMvc.perform(post("/v1/cores-workspace")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Erro de validação"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveListarTodasAsCores() throws Exception {

        Page<CorWorkspaceResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTodasCoresWorkspaceUseCase.execute(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/cores-workspace"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_cor").value(1))
                .andExpect(jsonPath("$.content[0].cor").value("#0000FF"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveBuscarCorPorIdComSucesso() throws Exception {

        when(buscarCorWorkspacePorIdUserCase.execute(1L))
                .thenReturn(response);

        mockMvc.perform(get("/v1/cores-workspace/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_cor").value(1))
                .andExpect(jsonPath("$.cor").value("#0000FF"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoCorNaoForEncontrada() throws Exception {

        when(buscarCorWorkspacePorIdUserCase.execute(999L))
                .thenThrow(new EntityNotFoundException(
                        "Cor do Workspace com id: 999 não encontrada."
                ));

        mockMvc.perform(get("/v1/cores-workspace/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cor do Workspace com id: 999 não encontrada."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveDesativarCorComSucesso() throws Exception {

        doNothing().when(desativarCorWorkspaceUseCase)
                .execute(1L);

        mockMvc.perform(delete("/v1/cores-workspace/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404AoDesativarCorNaoEncontrada() throws Exception {

        doThrow(new EntityNotFoundException(
                "Cor do Workspace com id: 999 não encontrada."))
                .when(desativarCorWorkspaceUseCase)
                .execute(999L);

        mockMvc.perform(delete("/v1/cores-workspace/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cor do Workspace com id: 999 não encontrada."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtivarCorComSucesso() throws Exception {

        doNothing().when(ativarCorWorkspaceUseCase)
                .execute(1L);

        mockMvc.perform(patch("/v1/cores-workspace/ativar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404AoAtivarCorNaoEncontrada() throws Exception {

        doThrow(new EntityNotFoundException(
                "Cor do Workspace com id: 999 não encontrada."))
                .when(ativarCorWorkspaceUseCase)
                .execute(999L);

        mockMvc.perform(patch("/v1/cores-workspace/ativar/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cor do Workspace com id: 999 não encontrada."));
    }
}