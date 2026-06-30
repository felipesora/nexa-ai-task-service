package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.corTag.CorTagRequestDTO;
import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.usecase.corTag.AtivarCorTagUseCase;
import com.nexa.task.application.usecase.corTag.BuscarCorTagPorIdUseCase;
import com.nexa.task.application.usecase.corTag.CadastrarCorTagUseCase;
import com.nexa.task.application.usecase.corTag.DesativarCorTagUseCase;
import com.nexa.task.application.usecase.corTag.ListarTodasCoresTagUseCase;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/cores-tag")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cores de Tag", description = "Operacoes para gerenciamento das cores utilizadas nas tags.")
public class CorTagController {

    private final CadastrarCorTagUseCase cadastrarCorTagUseCase;
    private final ListarTodasCoresTagUseCase listarTodasCoresTagUseCase;
    private final BuscarCorTagPorIdUseCase buscarCorTagPorIdUseCase;
    private final DesativarCorTagUseCase desativarCorTagUseCase;
    private final AtivarCorTagUseCase ativarCorTagUseCase;

    public CorTagController(
            CadastrarCorTagUseCase cadastrarCorTagUseCase,
            ListarTodasCoresTagUseCase listarTodasCoresTagUseCase,
            BuscarCorTagPorIdUseCase buscarCorTagPorIdUseCase,
            DesativarCorTagUseCase desativarCorTagUseCase,
            AtivarCorTagUseCase ativarCorTagUseCase) {
        this.cadastrarCorTagUseCase = cadastrarCorTagUseCase;
        this.listarTodasCoresTagUseCase = listarTodasCoresTagUseCase;
        this.buscarCorTagPorIdUseCase = buscarCorTagPorIdUseCase;
        this.desativarCorTagUseCase = desativarCorTagUseCase;
        this.ativarCorTagUseCase = ativarCorTagUseCase;
    }

    @Operation(
            summary = "Cadastrar cor de tag",
            description = """
                Cria uma nova cor de tag.

                Regras de negocio:
                - A cor deve ter entre 3 e 50 caracteres.
                - O valor da cor deve ser unico no sistema.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cor de tag cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = CorTagResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou cor ja cadastrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CorTagResponseDTO> cadastrarCor(
            @RequestBody @Valid CorTagRequestDTO request,
            UriComponentsBuilder uriBuilder) {
        CorTagResponseDTO response = cadastrarCorTagUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/cores-tag/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(
            summary = "Listar cores de tag",
            description = """
                Retorna uma lista paginada de cores de tag.

                Regras de negocio:
                - O retorno contem os registros paginados do catalogo de cores de tag.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cores de tag retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<CorTagResponseDTO>> listarTodasCores(@PageableDefault(size = 10) Pageable pageable) {
        Page<CorTagResponseDTO> cores = listarTodasCoresTagUseCase.execute(pageable);
        return ResponseEntity.ok(cores);
    }

    @Operation(
            summary = "Buscar cor de tag por ID",
            description = """
                Retorna os dados de uma cor de tag.

                Regras de negocio:
                - A cor de tag deve existir.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cor de tag encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = CorTagResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor de tag nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CorTagResponseDTO> buscarCorPorId(
            @Parameter(description = "Identificador da cor de tag") @PathVariable Long id) {
        CorTagResponseDTO cor = buscarCorTagPorIdUseCase.execute(id);
        return ResponseEntity.ok(cor);
    }

    @Operation(
            summary = "Desativar cor de tag",
            description = """
                Realiza a desativacao logica de uma cor de tag.

                Regras de negocio:
                - A cor de tag deve existir.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cor de tag desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor de tag nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarCor(
            @Parameter(description = "Identificador da cor de tag") @PathVariable Long id) {
        desativarCorTagUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar cor de tag",
            description = """
                Reativa uma cor de tag previamente desativada.

                Regras de negocio:
                - A cor de tag deve existir.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cor de tag ativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor de tag nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarCor(
            @Parameter(description = "Identificador da cor de tag") @PathVariable Long id) {
        ativarCorTagUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
