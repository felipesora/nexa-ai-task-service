package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.corTag.CorTagRequestDTO;
import com.nexa.task.application.dto.corTag.CorTagResponseDTO;
import com.nexa.task.application.usecase.corTag.*;
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
@RequestMapping("/v1/cores-tag")
@Tag(name = "Cores de Tag", description = "Operações para gerenciamento das cores utilizadas nas tags.")
public class CorTagController {

    private final CadastrarCorTagUseCase cadastrarCorTagUseCase;
    private final ListarTodasCoresTagUseCase listarTodasCoresTagUseCase;
    private final BuscarCorTagPorIdUseCase buscarCorTagPorIdUseCase;
    private final DesativarCorTagUseCase desativarCorTagUseCase;
    private final AtivarCorTagUseCase ativarCorTagUseCase;

    public CorTagController(CadastrarCorTagUseCase cadastrarCorTagUseCase, ListarTodasCoresTagUseCase listarTodasCoresTagUseCase, BuscarCorTagPorIdUseCase buscarCorTagPorIdUseCase, DesativarCorTagUseCase desativarCorTagUseCase, AtivarCorTagUseCase ativarCorTagUseCase) {
        this.cadastrarCorTagUseCase = cadastrarCorTagUseCase;
        this.listarTodasCoresTagUseCase = listarTodasCoresTagUseCase;
        this.buscarCorTagPorIdUseCase = buscarCorTagPorIdUseCase;
        this.desativarCorTagUseCase = desativarCorTagUseCase;
        this.ativarCorTagUseCase = ativarCorTagUseCase;
    }

    @Operation(summary = "Cadastrar cor de tag",
            description = """
                Cria uma nova cor de tag.

                A cor deve ser única no sistema.
                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem executar esta operação.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cor cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = CorTagResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<CorTagResponseDTO> cadastrarCor(@RequestBody @Valid CorTagRequestDTO request,
                                                                UriComponentsBuilder uriBuilder) {
        CorTagResponseDTO response = cadastrarCorTagUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/cores-tag/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(summary = "Listar cores de tag",
            description = """
                Retorna uma lista paginada de cores de tag.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cores retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<CorTagResponseDTO>> listarTodasCores(@PageableDefault(size = 10) Pageable pageable) {
        Page<CorTagResponseDTO> cores = listarTodasCoresTagUseCase.execute(pageable);
        return ResponseEntity.ok(cores);
    }

    @Operation(summary = "Buscar cor de tag por ID",
            description = """
                Retorna os dados de uma cor de tag.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cor encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = CorTagResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ícone não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CorTagResponseDTO> buscarCorPorId(@PathVariable Long id) {
        CorTagResponseDTO cor = buscarCorTagPorIdUseCase.execute(id);
        return ResponseEntity.ok(cor);
    }

    @Operation(summary = "Desativar cor de tag",
            description = """
                Realiza a desativação lógica de uma cor.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cor desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarCor(@PathVariable Long id) {
        desativarCorTagUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar cor de tag",
            description = """
                Reativa uma cor previamente desativada.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cor ativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarCor(@PathVariable Long id) {
        ativarCorTagUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
