package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.corTag.CorTagRequestDTO;
import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.exception.BadRequestException;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.corTag.*;
import com.nexa.task.domain.builder.tag.CorTagBuilder;
import com.nexa.task.domain.entity.tag.CorTag;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CorTagController.class)
class CorTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CadastrarCorTagUseCase cadastrarCorTagUseCase;

    @MockitoBean
    private ListarTodasCoresTagUseCase listarTodasCoresTagUseCase;

    @MockitoBean
    private BuscarCorTagPorIdUserCase buscarCorTagPorIdUserCase;

    @MockitoBean
    private DesativarCorTagUseCase desativarCorTagUseCase;

    @MockitoBean
    private AtivarCorTagUseCase ativarCorTagUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private CorTagRequestDTO request;
    private CorTag cor;
    private CorTagResponseDTO response;

    @BeforeEach
    void setUp() {

        request = new CorTagRequestDTO(
                "#FF0000"
        );

        cor = new CorTagBuilder()
                .comId(1L)
                .comCor("#FF0000")
                .comAtivo(true)
                .build();

        response = new CorTagResponseDTO(
                cor.getId(),
                cor.getCor(),
                cor.getAtivo()
        );
    }

    @Test
    void deveCadastrarCorComSucesso() throws Exception {

        when(cadastrarCorTagUseCase.execute(request))
                .thenReturn(response);

        mockMvc.perform(post("/v1/cores-tag")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/v1/cores-tag/1"))
                .andExpect(jsonPath("$.id_cor").value(1))
                .andExpect(jsonPath("$.cor").value("#FF0000"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveRetornar400QuandoCorJaExistir() throws Exception {

        when(cadastrarCorTagUseCase.execute(request))
                .thenThrow(new BadRequestException(
                        "Esta cor de tag já está cadastrada."
                ));

        mockMvc.perform(post("/v1/cores-tag")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Esta cor de tag já está cadastrada."));
    }

    @Test
    void deveRetornar400QuandoCorForInvalida() throws Exception {

        CorTagRequestDTO requestInvalido =
                new CorTagRequestDTO("");

        mockMvc.perform(post("/v1/cores-tag")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Erro de validação"));
    }

    @Test
    void deveListarTodasAsCores() throws Exception {

        Page<CorTagResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTodasCoresTagUseCase.execute(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/cores-tag"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_cor").value(1))
                .andExpect(jsonPath("$.content[0].cor").value("#FF0000"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deveBuscarCorPorIdComSucesso() throws Exception {

        when(buscarCorTagPorIdUserCase.execute(1L))
                .thenReturn(response);

        mockMvc.perform(get("/v1/cores-tag/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_cor").value(1))
                .andExpect(jsonPath("$.cor").value("#FF0000"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveRetornar404QuandoCorNaoForEncontrada() throws Exception {

        when(buscarCorTagPorIdUserCase.execute(999L))
                .thenThrow(new EntityNotFoundException(
                        "Cor da Tag com id: 999 não encontrada."
                ));

        mockMvc.perform(get("/v1/cores-tag/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cor da Tag com id: 999 não encontrada."));
    }

    @Test
    void deveDesativarCorComSucesso() throws Exception {

        doNothing().when(desativarCorTagUseCase)
                .execute(1L);

        mockMvc.perform(delete("/v1/cores-tag/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoDesativarCorNaoEncontrada() throws Exception {

        doThrow(new EntityNotFoundException(
                "Cor da Tag com id: 999 não encontrada."))
                .when(desativarCorTagUseCase)
                .execute(999L);

        mockMvc.perform(delete("/v1/cores-tag/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cor da Tag com id: 999 não encontrada."));
    }

    @Test
    void deveAtivarCorComSucesso() throws Exception {

        doNothing().when(ativarCorTagUseCase)
                .execute(1L);

        mockMvc.perform(patch("/v1/cores-tag/ativar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoAtivarCorNaoEncontrada() throws Exception {

        doThrow(new EntityNotFoundException(
                "Cor da Tag com id: 999 não encontrada."))
                .when(ativarCorTagUseCase)
                .execute(999L);

        mockMvc.perform(patch("/v1/cores-tag/ativar/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cor da Tag com id: 999 não encontrada."));
    }
}