package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.tag.TagCreateDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.dto.tag.TagUpdateDTO;
import com.nexa.task.application.usecase.tag.*;
import com.nexa.task.presentation.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/tags")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tags", description = "Operacoes para gerenciamento das tags.")
public class TagController {

    private final CadastrarTagUseCase cadastrarTagUseCase;
    private final ListarTodasTagsUseCase listarTodasTagsUseCase;
    private final ListarTagsPorIdUsuarioUseCase listarTagsPorIdUsuarioUseCase;
    private final BuscarTagPorIdUseCase buscarTagPorIdUseCase;
    private final AtualizarTagUseCase atualizarTagUseCase;
    private final AtivarTagUseCase ativarTagUseCase;
    private final DesativarTagUseCase desativarTagUseCase;

    public TagController(CadastrarTagUseCase cadastrarTagUseCase,
                         ListarTodasTagsUseCase listarTodasTagsUseCase,
                         ListarTagsPorIdUsuarioUseCase listarTagsPorIdUsuarioUseCase,
                         BuscarTagPorIdUseCase buscarTagPorIdUseCase,
                         AtualizarTagUseCase atualizarTagUseCase,
                         AtivarTagUseCase ativarTagUseCase,
                         DesativarTagUseCase desativarTagUseCase) {
        this.cadastrarTagUseCase = cadastrarTagUseCase;
        this.listarTodasTagsUseCase = listarTodasTagsUseCase;
        this.listarTagsPorIdUsuarioUseCase = listarTagsPorIdUsuarioUseCase;
        this.buscarTagPorIdUseCase = buscarTagPorIdUseCase;
        this.atualizarTagUseCase = atualizarTagUseCase;
        this.ativarTagUseCase = ativarTagUseCase;
        this.desativarTagUseCase = desativarTagUseCase;
    }

    @Operation(
            summary = "Cadastrar tag",
            description = """
                Cria uma nova tag para o usuario autenticado.

                Regras de negocio:
                - O nome da tag deve ter entre 3 e 100 caracteres.
                - O nome da tag deve ser unico por usuario.
                - A cor informada em `id_cor`, quando enviada, deve existir.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tag cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou nome de tag ja cadastrado para o usuario",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TagResponseDTO> cadastrarTag(@RequestBody @Valid TagCreateDTO request,
                                                          UriComponentsBuilder uriBuilder) {
        TagResponseDTO response = cadastrarTagUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/tags/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(
            summary = "Listar tags",
            description = """
                Retorna uma lista paginada de tags.

                Regras de negocio:
                - Endpoint restrito a usuarios com ROLE_ADMIN.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<TagResponseDTO>> listarTodasTags(@PageableDefault(size = 10) Pageable pageable) {
        Page<TagResponseDTO> tags = listarTodasTagsUseCase.execute(pageable);
        return ResponseEntity.ok(tags);
    }

    @Operation(
            summary = "Buscar tag por ID",
            description = """
                Retorna os dados de uma tag.

                Regras de negocio:
                - A tag deve existir.
                - O acesso e permitido para administradores ou para o proprietario da tag.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tag encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tag nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> buscarTagPorId(
            @Parameter(description = "Identificador da tag") @PathVariable Long id) {
        TagResponseDTO tag = buscarTagPorIdUseCase.execute(id);
        return ResponseEntity.ok(tag);
    }

    @Operation(
            summary = "Listar tags por usuario",
            description = """
                Retorna uma lista paginada das tags de um usuario.

                Regras de negocio:
                - Apenas administradores ou o proprio usuario podem consultar a lista.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<TagResponseDTO>> listarTagsPorIdUsuario(
            @Parameter(description = "Identificador do usuario proprietario das tags") @PathVariable Long idUsuario,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<TagResponseDTO> tags = listarTagsPorIdUsuarioUseCase.execute(idUsuario, pageable);
        return ResponseEntity.ok(tags);
    }

    @Operation(
            summary = "Atualizar tag",
            description = """
            Atualiza os dados editaveis de uma tag.

            Regras de negocio:
            - A tag deve existir.
            - Apenas o proprietario da tag ou usuarios com ROLE_ADMIN podem atualizar.
            - O nome da tag deve ter entre 3 e 100 caracteres.
            - O nome da tag deve continuar unico por usuario.
            - A cor informada em `id_cor`, quando enviada, deve existir.

            Requer autenticacao JWT.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag atualizada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou nome de tag ja cadastrado para o usuario", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tag ou cor nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarTag(
            @Parameter(description = "Identificador da tag") @PathVariable Long id,
            @RequestBody @Valid TagUpdateDTO dto) {
        atualizarTagUseCase.execute(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar tag",
            description = """
            Realiza a desativacao logica de uma tag.

            Regras de negocio:
            - A tag deve existir.
            - Apenas o proprietario da tag ou usuarios com ROLE_ADMIN podem desativar.
            - A operacao realiza exclusao logica; a tag nao e removida fisicamente.

            Requer autenticacao JWT.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tag nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarTag(
            @Parameter(description = "Identificador da tag") @PathVariable Long id) {
        desativarTagUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar tag",
            description = """
            Reativa uma tag previamente desativada.

            Regras de negocio:
            - A tag deve existir.
            - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.
            - A operacao remove a marcacao de desativacao logica da tag.

            Requer autenticacao JWT.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag ativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tag nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarTag(
            @Parameter(description = "Identificador da tag") @PathVariable Long id) {
        ativarTagUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
