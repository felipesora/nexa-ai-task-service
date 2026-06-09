package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.usecase.iconeWorkspace.CadastrarIconeWorkspaceUseCase;
import com.nexa.task.domain.builder.workspace.IconeWorkspaceBuilder;
import com.nexa.task.domain.entity.workspace.IconeWorkspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IconeWorkspaceController.class)
class IconeWorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase;

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
}