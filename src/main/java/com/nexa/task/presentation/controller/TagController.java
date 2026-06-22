package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.tag.TagCreateDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.dto.tag.TagUpdateDTO;
import com.nexa.task.application.usecase.tag.*;
import com.nexa.task.presentation.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/tags")
@Tag(name = "Tags", description = "Operações para gerenciamento das tags.")
public class TagController {

    private final CadastrarTagUseCase cadastrarTagUseCase;
    private final ListarTodasTagsUseCase listarTodasTagsUseCase;
    private final ListarTagsPorIdUsuarioUseCase listarTagsPorIdUsuarioUseCase;
    private final BuscarTagPorIdUseCase buscarTagPorIdUseCase;
    private final AtualizarTagUseCase atualizarTagUseCase;
    private final AtivarTagUseCase ativarTagUseCase;
    private final DesativarTagUseCase desativarTagUseCase;

    public TagController(CadastrarTagUseCase cadastrarTagUseCase, ListarTodasTagsUseCase listarTodasTagsUseCase, ListarTagsPorIdUsuarioUseCase listarTagsPorIdUsuarioUseCase, BuscarTagPorIdUseCase buscarTagPorIdUseCase, AtualizarTagUseCase atualizarTagUseCase, AtivarTagUseCase ativarTagUseCase, DesativarTagUseCase desativarTagUseCase) {
        this.cadastrarTagUseCase = cadastrarTagUseCase;
        this.listarTodasTagsUseCase = listarTodasTagsUseCase;
        this.listarTagsPorIdUsuarioUseCase = listarTagsPorIdUsuarioUseCase;
        this.buscarTagPorIdUseCase = buscarTagPorIdUseCase;
        this.atualizarTagUseCase = atualizarTagUseCase;
        this.ativarTagUseCase = ativarTagUseCase;
        this.desativarTagUseCase = desativarTagUseCase;
    }

    @Operation(summary = "Cadastrar tag",
            description = """
                Cria uma nova tag.

                Requer autenticação JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tag cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<TagResponseDTO> cadastrarTag(@RequestBody @Valid TagCreateDTO request,
                                                          UriComponentsBuilder uriBuilder) {
        TagResponseDTO response = cadastrarTagUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/tags/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(summary = "Listar tags",
            description = """
                Retorna uma lista paginada de tags.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<TagResponseDTO>> listarTodasTags(@PageableDefault(size = 10) Pageable pageable) {
        Page<TagResponseDTO> tags = listarTodasTagsUseCase.execute(pageable);
        return ResponseEntity.ok(tags);
    }

    @Operation(summary = "Buscar tag por ID",
            description = """
                Retorna os dados de uma tag.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário da tag solicitada.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> buscarTagPorId(@PathVariable Long id) {
        TagResponseDTO tag = buscarTagPorIdUseCase.execute(id);
        return ResponseEntity.ok(tag);
    }

    @Operation(summary = "Listar tags por ID do usuário",
            description = """
                Retorna uma lista paginada de tags de um usuário.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário das tags solicitadas.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<TagResponseDTO>> listarTagsPorIdUsuario(@PathVariable Long idUsuario,
                                                                                   @PageableDefault(size = 10) Pageable pageable) {
        Page<TagResponseDTO> tags = listarTagsPorIdUsuarioUseCase.execute(idUsuario, pageable);
        return ResponseEntity.ok(tags);
    }

    @Operation(
            summary = "Atualizar tag",
            description = """
            Atualiza os dados de uma tag.

            Apenas o dono da tag pode atualizar os dados,
            exceto administradores.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag atualizada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tag ou cor não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarTag(@PathVariable Long id, @RequestBody @Valid TagUpdateDTO dto) {
        atualizarTagUseCase.execute(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar tag",
            description = """
            Realiza a desativação lógica de uma tag.

            Apenas o dono da tag pode desativá-la,
            exceto administradores.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarTag(@PathVariable Long id) {
        desativarTagUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar tag",
            description = """
            Reativa uma tag previamente desativada.

            Apenas administradores podem executar esta operação.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag ativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tag não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarTag(@PathVariable Long id) {
        ativarTagUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
