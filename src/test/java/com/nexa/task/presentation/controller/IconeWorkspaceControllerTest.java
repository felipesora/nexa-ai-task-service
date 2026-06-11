package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.iconeWorkspace.*;
import com.nexa.task.domain.builder.workspace.IconeWorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IconeWorkspaceController.class)
class IconeWorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase;

    @MockitoBean
    private ListarTodosIconesWorkspaceUseCase listarTodosIconesWorkspaceUseCase;

    @MockitoBean
    private BuscarIconeWorkspacePorIdUserCase buscarIconeWorkspacePorIdUserCase;

    @MockitoBean
    private DesativarIconeWorkspaceUseCase desativarIconeWorkspaceUseCase;

    @MockitoBean
    private AtivarIconeWorkspaceUseCase ativarIconeWorkspaceUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private IconeWorkspaceRequestDTO request;
    private IconeWorkspace icone;
    private IconeWorkspaceResponseDTO response;

    @BeforeEach
    void setUp() {

        request = new IconeWorkspaceRequestDTO(
                "ícone padrão",
                "icon-padrao.png"
        );

        icone = new IconeWorkspaceBuilder()
                .comId(1L)
                .comNome("ícone padrão")
                .comCaminho("icon-padrao.png")
                .comAtivo(true)
                .build();

        response = new IconeWorkspaceResponseDTO(
                icone.getId(),
                icone.getNome(),
                icone.getCaminho(),
                icone.getAtivo()
        );
    }

    @Test
    void deveCadastrarIconeComSucesso() throws Exception {

        when(cadastrarIconeWorkspaceUseCase.execute(request)).thenReturn(response);

        mockMvc.perform(post("/v1/icones-workspace")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost/v1/icones-workspace/1"))
                .andExpect(jsonPath("$.id_icone").value(1))
                .andExpect(jsonPath("$.nome").value("ícone padrão"))
                .andExpect(jsonPath("$.caminho").value("icon-padrao.png"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveRetornar400QuandoNomeJaExistir() throws Exception {

        when(cadastrarIconeWorkspaceUseCase.execute(request))
                .thenThrow(new BadRequestException("Este nome de ícone já está cadastrado."));

        mockMvc.perform(post("/v1/icones-workspace")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Este nome de ícone já está cadastrado."));
    }

    @Test
    void deveRetornar400QuandoCaminhoJaExistir() throws Exception {

        when(cadastrarIconeWorkspaceUseCase.execute(request))
                .thenThrow(new BadRequestException("Este caminho de ícone já está cadastrado."));

        mockMvc.perform(post("/v1/icones-workspace")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Este caminho de ícone já está cadastrado."));
    }

    @Test
    void deveRetornar400QuandoNomeForInvalido() throws Exception {

        IconeWorkspaceRequestDTO requestInvalido =
                new IconeWorkspaceRequestDTO("", "");

        mockMvc.perform(post("/v1/icones-workspace")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erro de validação"));
    }

    @Test
    void deveListarTodosOsIcones() throws Exception {

        Page<IconeWorkspaceResponseDTO> page = new PageImpl<>(List.of(response));

        when(listarTodosIconesWorkspaceUseCase.execute(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/v1/icones-workspace"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_icone").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("ícone padrão"))
                .andExpect(jsonPath("$.content[0].caminho").value("icon-padrao.png"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarIconePorIdComSucesso() throws Exception {

        when(buscarIconeWorkspacePorIdUserCase.execute(1L))
                .thenReturn(response);

        mockMvc.perform(get("/v1/icones-workspace/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_icone").value(1))
                .andExpect(jsonPath("$.nome").value("ícone padrão"))
                .andExpect(jsonPath("$.caminho").value("icon-padrao.png"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveRetornar404QuandoIconeNaoForEncontrado() throws Exception {

        when(buscarIconeWorkspacePorIdUserCase.execute(999L))
                .thenThrow(new EntityNotFoundException("Ícone com id: 999 não encontrado."));

        mockMvc.perform(get("/v1/icones-workspace/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Ícone com id: 999 não encontrado."));
    }

    @Test
    void deveDesativarIconeComSucesso() throws Exception {

        doNothing().when(desativarIconeWorkspaceUseCase)
                .execute(1L);

        mockMvc.perform(delete("/v1/icones-workspace/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoDesativarIconeNaoEncontrado() throws Exception {

        doThrow(new EntityNotFoundException(
                "Ícone com id: 999 não encontrado."))
                .when(desativarIconeWorkspaceUseCase)
                .execute(999L);

        mockMvc.perform(delete("/v1/icones-workspace/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Ícone com id: 999 não encontrado."));
    }

    @Test
    void deveAtivarIconeComSucesso() throws Exception {

        doNothing().when(ativarIconeWorkspaceUseCase)
                .execute(1L);

        mockMvc.perform(patch("/v1/icones-workspace/ativar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoAtivarIconeNaoEncontrado() throws Exception {

        doThrow(new EntityNotFoundException(
                "Ícone com id: 999 não encontrado."))
                .when(ativarIconeWorkspaceUseCase)
                .execute(999L);

        mockMvc.perform(patch("/v1/icones-workspace/ativar/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Ícone com id: 999 não encontrado."));
    }
}