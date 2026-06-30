package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.corWorkspace.CorWorkspaceRequestDTO;
import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.usecase.corWorkspace.AtivarCorWorkspaceUseCase;
import com.nexa.task.application.usecase.corWorkspace.BuscarCorWorkspacePorIdUserCase;
import com.nexa.task.application.usecase.corWorkspace.CadastrarCorWorkspaceUseCase;
import com.nexa.task.application.usecase.corWorkspace.DesativarCorWorkspaceUseCase;
import com.nexa.task.application.usecase.corWorkspace.ListarTodasCoresWorkspaceUseCase;
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
@RequestMapping("/v1/cores-workspace")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cores de Workspace", description = "Operacoes para gerenciamento das cores utilizadas nos workspaces.")
public class CorWorkspaceController {

    private final CadastrarCorWorkspaceUseCase cadastrarCorWorkspaceUseCase;
    private final ListarTodasCoresWorkspaceUseCase listarTodasCoresWorkspaceUseCase;
    private final BuscarCorWorkspacePorIdUserCase buscarCorWorkspacePorIdUserCase;
    private final DesativarCorWorkspaceUseCase desativarCorWorkspaceUseCase;
    private final AtivarCorWorkspaceUseCase ativarCorWorkspaceUseCase;

    public CorWorkspaceController(
            CadastrarCorWorkspaceUseCase cadastrarCorWorkspaceUseCase,
            ListarTodasCoresWorkspaceUseCase listarTodasCoresWorkspaceUseCase,
            BuscarCorWorkspacePorIdUserCase buscarCorWorkspacePorIdUserCase,
            DesativarCorWorkspaceUseCase desativarCorWorkspaceUseCase,
            AtivarCorWorkspaceUseCase ativarCorWorkspaceUseCase) {
        this.cadastrarCorWorkspaceUseCase = cadastrarCorWorkspaceUseCase;
        this.listarTodasCoresWorkspaceUseCase = listarTodasCoresWorkspaceUseCase;
        this.buscarCorWorkspacePorIdUserCase = buscarCorWorkspacePorIdUserCase;
        this.desativarCorWorkspaceUseCase = desativarCorWorkspaceUseCase;
        this.ativarCorWorkspaceUseCase = ativarCorWorkspaceUseCase;
    }

    @Operation(
            summary = "Cadastrar cor de workspace",
            description = """
                Cria uma nova cor de workspace.

                Regras de negocio:
                - A cor deve ter entre 3 e 50 caracteres.
                - O valor da cor deve ser unico no sistema.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cor de workspace cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = CorWorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou cor ja cadastrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CorWorkspaceResponseDTO> cadastrarCor(
            @RequestBody @Valid CorWorkspaceRequestDTO request,
            UriComponentsBuilder uriBuilder) {
        CorWorkspaceResponseDTO response = cadastrarCorWorkspaceUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/cores-workspace/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(
            summary = "Listar cores de workspace",
            description = """
                Retorna uma lista paginada de cores de workspace.

                Regras de negocio:
                - O retorno contem os registros paginados do catalogo de cores de workspace.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cores de workspace retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<CorWorkspaceResponseDTO>> listarTodasCores(@PageableDefault(size = 10) Pageable pageable) {
        Page<CorWorkspaceResponseDTO> cores = listarTodasCoresWorkspaceUseCase.execute(pageable);
        return ResponseEntity.ok(cores);
    }

    @Operation(
            summary = "Buscar cor de workspace por ID",
            description = """
                Retorna os dados de uma cor de workspace.

                Regras de negocio:
                - A cor de workspace deve existir.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cor de workspace encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = CorWorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor de workspace nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CorWorkspaceResponseDTO> buscarCorPorId(
            @Parameter(description = "Identificador da cor de workspace") @PathVariable Long id) {
        CorWorkspaceResponseDTO cor = buscarCorWorkspacePorIdUserCase.execute(id);
        return ResponseEntity.ok(cor);
    }

    @Operation(
            summary = "Desativar cor de workspace",
            description = """
                Realiza a desativacao logica de uma cor de workspace.

                Regras de negocio:
                - A cor de workspace deve existir.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cor de workspace desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor de workspace nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarCor(
            @Parameter(description = "Identificador da cor de workspace") @PathVariable Long id) {
        desativarCorWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar cor de workspace",
            description = """
                Reativa uma cor de workspace previamente desativada.

                Regras de negocio:
                - A cor de workspace deve existir.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cor de workspace ativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor de workspace nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarCor(
            @Parameter(description = "Identificador da cor de workspace") @PathVariable Long id) {
        ativarCorWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
