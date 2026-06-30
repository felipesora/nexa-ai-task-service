package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.usecase.iconeWorkspace.*;
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
@RequestMapping("/v1/icones-workspace")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Ícones de Workspace", description = "Operações para gerenciamento dos ícones utilizados nos workspaces.")
public class IconeWorkspaceController {

    private final CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase;
    private final ListarTodosIconesWorkspaceUseCase listarTodosIconesWorkspaceUseCase;
    private final BuscarIconeWorkspacePorIdUserCase buscarIconeWorkspacePorIdUserCase;
    private final DesativarIconeWorkspaceUseCase desativarIconeWorkspaceUseCase;
    private final AtivarIconeWorkspaceUseCase ativarIconeWorkspaceUseCase;

    public IconeWorkspaceController(CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase, ListarTodosIconesWorkspaceUseCase listarTodosIconesWorkspaceUseCase, BuscarIconeWorkspacePorIdUserCase buscarIconeWorkspacePorIdUserCase, DesativarIconeWorkspaceUseCase desativarIconeWorkspaceUseCase, AtivarIconeWorkspaceUseCase ativarIconeWorkspaceUseCase) {
        this.cadastrarIconeWorkspaceUseCase = cadastrarIconeWorkspaceUseCase;
        this.listarTodosIconesWorkspaceUseCase = listarTodosIconesWorkspaceUseCase;
        this.buscarIconeWorkspacePorIdUserCase = buscarIconeWorkspacePorIdUserCase;
        this.desativarIconeWorkspaceUseCase = desativarIconeWorkspaceUseCase;
        this.ativarIconeWorkspaceUseCase = ativarIconeWorkspaceUseCase;
    }

    @Operation(summary = "Cadastrar ícone de workspace",
            description = """
                Cria um novo ícone de workspace.

                O nome e o caminho do ícone devem ser únicos no sistema.
                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem executar esta operação.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ícone cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = IconeWorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<IconeWorkspaceResponseDTO> cadastrarIcone(@RequestBody @Valid IconeWorkspaceRequestDTO request,
                                                                    UriComponentsBuilder uriBuilder) {
        IconeWorkspaceResponseDTO response = cadastrarIconeWorkspaceUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/icones-workspace/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(summary = "Listar ícones de workspace",
            description = """
                Retorna uma lista paginada de ícones de workspace.

                Requer autenticação JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ícones retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<IconeWorkspaceResponseDTO>> listarTodosIcones(@PageableDefault(size = 10)Pageable pageable) {
        Page<IconeWorkspaceResponseDTO> icones = listarTodosIconesWorkspaceUseCase.execute(pageable);
        return ResponseEntity.ok(icones);
    }

    @Operation(summary = "Buscar ícone de workspace por ID",
            description = """
                Retorna os dados de um ícone de workspace.

                Requer autenticação JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ícone encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = IconeWorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ícone não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<IconeWorkspaceResponseDTO> buscarIconePorId(@PathVariable Long id) {
        IconeWorkspaceResponseDTO icone = buscarIconeWorkspacePorIdUserCase.execute(id);
        return ResponseEntity.ok(icone);
    }

    @Operation(summary = "Desativar ícone de workspace",
            description = """
                Realiza a desativação lógica de um ícone.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ícone desativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ícone não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarIcone(@PathVariable Long id) {
        desativarIconeWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar ícone de workspace",
            description = """
                Reativa um ícone previamente desativado.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ícone ativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ícone não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarIcone(@PathVariable Long id) {
        ativarIconeWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
