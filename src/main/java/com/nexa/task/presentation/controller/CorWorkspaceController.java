package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.corWorkspace.CorWorkspaceRequestDTO;
import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.usecase.corWorkspace.*;
import com.nexa.task.presentation.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/v1/cores-workspace")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cores de Workspace", description = "Operações para gerenciamento das cores utilizadas nos workspaces.")
public class CorWorkspaceController {

    private final CadastrarCorWorkspaceUseCase cadastrarCorWorkspaceUseCase;
    private final ListarTodasCoresWorkspaceUseCase listarTodasCoresWorkspaceUseCase;
    private final BuscarCorWorkspacePorIdUserCase buscarCorWorkspacePorIdUserCase;
    private final DesativarCorWorkspaceUseCase desativarCorWorkspaceUseCase;
    private final AtivarCorWorkspaceUseCase ativarCorWorkspaceUseCase;

    public CorWorkspaceController(CadastrarCorWorkspaceUseCase cadastrarCorWorkspaceUseCase, ListarTodasCoresWorkspaceUseCase listarTodasCoresWorkspaceUseCase, BuscarCorWorkspacePorIdUserCase buscarCorWorkspacePorIdUserCase, DesativarCorWorkspaceUseCase desativarCorWorkspaceUseCase, AtivarCorWorkspaceUseCase ativarCorWorkspaceUseCase) {
        this.cadastrarCorWorkspaceUseCase = cadastrarCorWorkspaceUseCase;
        this.listarTodasCoresWorkspaceUseCase = listarTodasCoresWorkspaceUseCase;
        this.buscarCorWorkspacePorIdUserCase = buscarCorWorkspacePorIdUserCase;
        this.desativarCorWorkspaceUseCase = desativarCorWorkspaceUseCase;
        this.ativarCorWorkspaceUseCase = ativarCorWorkspaceUseCase;
    }

    @Operation(summary = "Cadastrar cor de workspace",
            description = """
                Cria uma nova cor de workspace.

                A cor deve ser única no sistema.
                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem executar esta operação.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cor cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = CorWorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CorWorkspaceResponseDTO> cadastrarCor(@RequestBody @Valid CorWorkspaceRequestDTO request,
                                                                    UriComponentsBuilder uriBuilder) {
        CorWorkspaceResponseDTO response = cadastrarCorWorkspaceUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/cores-workspace/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(summary = "Listar cores de workspace",
            description = """
                Retorna uma lista paginada de cores de workspace.

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
    public ResponseEntity<Page<CorWorkspaceResponseDTO>> listarTodasCores(@PageableDefault(size = 10) Pageable pageable) {
        Page<CorWorkspaceResponseDTO> cores = listarTodasCoresWorkspaceUseCase.execute(pageable);
        return ResponseEntity.ok(cores);
    }

    @Operation(summary = "Buscar cor de workspace por ID",
            description = """
                Retorna os dados de uma cor de workspace.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cor encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = CorWorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ícone não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CorWorkspaceResponseDTO> buscarCorPorId(@PathVariable Long id) {
        CorWorkspaceResponseDTO cor = buscarCorWorkspacePorIdUserCase.execute(id);
        return ResponseEntity.ok(cor);
    }

    @Operation(summary = "Desativar cor de workspace",
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
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarCor(@PathVariable Long id) {
        desativarCorWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar cor de workspace",
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
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarCor(@PathVariable Long id) {
        ativarCorWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
