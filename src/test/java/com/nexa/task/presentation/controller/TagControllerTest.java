package com.nexa.task.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.dto.tag.TagCreateDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.dto.tag.TagUpdateDTO;
import com.nexa.task.application.exception.EntityNotFoundException;
import com.nexa.task.application.usecase.tag.*;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
@AutoConfigureMockMvc(addFilters = false)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CadastrarTagUseCase cadastrarTagUseCase;

    @MockitoBean
    private ListarTodasTagsUseCase listarTodasTagsUseCase;

    @MockitoBean
    private ListarTagsPorIdUsuarioUseCase listarTagsPorIdUsuarioUseCase;

    @MockitoBean
    private BuscarTagPorIdUseCase buscarTagPorIdUseCase;

    @MockitoBean
    private AtualizarTagUseCase atualizarTagUseCase;

    @MockitoBean
    private AtivarTagUseCase ativarTagUseCase;

    @MockitoBean
    private DesativarTagUseCase desativarTagUseCase;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private TagCreateDTO request;
    private TagUpdateDTO updateDto;
    private TagResponseDTO response;

    @BeforeEach
    void setUp() {

        request = new TagCreateDTO(
                "Urgente",
                10L
        );

        updateDto = new TagUpdateDTO(
                "Importante",
                10L
        );

        CorTagResponseDTO corTag = new CorTagResponseDTO(
                10L,
                "#FF0000",
                true
        );

        response = new TagResponseDTO(
                1L,
                1L,
                "Urgente",
                true,
                corTag
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCadastrarTagComSucesso() throws Exception {

        when(cadastrarTagUseCase.execute(request))
                .thenReturn(response);

        mockMvc.perform(post("/v1/tags")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "http://localhost/v1/tags/1"))
                .andExpect(jsonPath("$.id_tag").value(1))
                .andExpect(jsonPath("$.id_usuario").value(1))
                .andExpect(jsonPath("$.nome").value("Urgente"))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.cor_tag.id_cor").value(10))
                .andExpect(jsonPath("$.cor_tag.cor").value("#FF0000"))
                .andExpect(jsonPath("$.cor_tag.ativo").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoCorNaoForEncontradaAoCadastrar() throws Exception {

        when(cadastrarTagUseCase.execute(request))
                .thenThrow(new EntityNotFoundException(
                        "Cor com id: 10 nÃ£o encontrada"
                ));

        mockMvc.perform(post("/v1/tags")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cor com id: 10 nÃ£o encontrada"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar400QuandoRequestForInvalido() throws Exception {

        TagCreateDTO requestInvalido = new TagCreateDTO(
                "",
                10L
        );

        mockMvc.perform(post("/v1/tags")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Erro de validação"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveListarTodasAsTags() throws Exception {

        Page<TagResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTodasTagsUseCase.execute(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_tag").value(1))
                .andExpect(jsonPath("$.content[0].id_usuario").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Urgente"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.content[0].cor_tag.id_cor").value(10))
                .andExpect(jsonPath("$.content[0].cor_tag.cor").value("#FF0000"))
                .andExpect(jsonPath("$.content[0].cor_tag.ativo").value(true))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveBuscarTagPorIdComSucesso() throws Exception {

        when(buscarTagPorIdUseCase.execute(1L))
                .thenReturn(response);

        mockMvc.perform(get("/v1/tags/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_tag").value(1))
                .andExpect(jsonPath("$.id_usuario").value(1))
                .andExpect(jsonPath("$.nome").value("Urgente"))
                .andExpect(jsonPath("$.ativo").value(true))
                .andExpect(jsonPath("$.cor_tag.id_cor").value(10))
                .andExpect(jsonPath("$.cor_tag.cor").value("#FF0000"))
                .andExpect(jsonPath("$.cor_tag.ativo").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoTagNaoForEncontrada() throws Exception {

        when(buscarTagPorIdUseCase.execute(999L))
                .thenThrow(new EntityNotFoundException(
                        "Tag com id: 999 nÃ£o encontrada."
                ));

        mockMvc.perform(get("/v1/tags/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tag com id: 999 nÃ£o encontrada."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveListarTagsPorIdUsuario() throws Exception {

        Page<TagResponseDTO> page =
                new PageImpl<>(List.of(response));

        when(listarTagsPorIdUsuarioUseCase
                .execute(eq(1L), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/v1/tags/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id_tag").value(1))
                .andExpect(jsonPath("$.content[0].id_usuario").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("Urgente"))
                .andExpect(jsonPath("$.content[0].ativo").value(true))
                .andExpect(jsonPath("$.content[0].cor_tag.id_cor").value(10))
                .andExpect(jsonPath("$.content[0].cor_tag.cor").value("#FF0000"))
                .andExpect(jsonPath("$.content[0].cor_tag.ativo").value(true))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarTagComSucesso() throws Exception {

        doNothing().when(atualizarTagUseCase)
                .execute(1L, updateDto);

        mockMvc.perform(
                        put("/v1/tags/1")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoTagNaoForEncontradaAoAtualizar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Tag com id: 999 nÃ£o encontrada."
        ))
                .when(atualizarTagUseCase)
                .execute(eq(999L), any(TagUpdateDTO.class));

        mockMvc.perform(
                        put("/v1/tags/999")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tag com id: 999 nÃ£o encontrada."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoCorNaoForEncontradaAoAtualizar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Cor com id: 10 nÃ£o encontrada"
        ))
                .when(atualizarTagUseCase)
                .execute(eq(1L), any(TagUpdateDTO.class));

        mockMvc.perform(
                        put("/v1/tags/1")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Cor com id: 10 nÃ£o encontrada"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar400QuandoRequestDeAtualizacaoForInvalido() throws Exception {

        TagUpdateDTO dtoInvalido = new TagUpdateDTO(
                "",
                10L
        );

        mockMvc.perform(
                        put("/v1/tags/1")
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
    void deveDesativarTagComSucesso() throws Exception {

        doNothing().when(desativarTagUseCase)
                .execute(1L);

        mockMvc.perform(delete("/v1/tags/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoTagNaoForEncontradaAoDesativar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Tag com id: 999 nÃ£o encontrada."
        ))
                .when(desativarTagUseCase)
                .execute(999L);

        mockMvc.perform(delete("/v1/tags/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tag com id: 999 nÃ£o encontrada."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveAtivarTagComSucesso() throws Exception {

        doNothing().when(ativarTagUseCase)
                .execute(1L);

        mockMvc.perform(patch("/v1/tags/ativar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404QuandoTagNaoForEncontradaAoAtivar() throws Exception {

        doThrow(new EntityNotFoundException(
                "Tag com id: 999 nÃ£o encontrada."
        ))
                .when(ativarTagUseCase)
                .execute(999L);

        mockMvc.perform(patch("/v1/tags/ativar/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Tag com id: 999 nÃ£o encontrada."));
    }
}
