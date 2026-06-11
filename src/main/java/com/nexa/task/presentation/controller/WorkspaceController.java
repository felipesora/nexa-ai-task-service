package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.workspace.WorkspaceRequestDTO;
import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.usecase.workspace.*;
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
@RequestMapping("/v1/workspaces")
@Tag(name = "Workspaces", description = "Operações para gerenciamento dos workspaces.")
public class WorkspaceController
{
    private final CadastrarWorkspaceUseCase cadastrarWorkspaceUseCase;
    private final ListarTodosWorkspacesUseCase listarTodosWorkspacesUseCase;
    private final BuscarWorkspacePorIdUseCase buscarWorkspacePorIdUseCase;
    private final ListarWorkspacesPorIdUsuarioUseCase listarWorkspacesPorIdUsuarioUseCase;
    private final ListarWorkspacesPorIdUsuarioENomeUseCase listarWorkspacesPorIdUsuarioENomeUseCase;
    private final AtualizarWorkspaceUseCase atualizarWorkspaceUseCase;

    public WorkspaceController(CadastrarWorkspaceUseCase cadastrarWorkspaceUseCase, ListarTodosWorkspacesUseCase listarTodosWorkspacesUseCase, BuscarWorkspacePorIdUseCase buscarWorkspacePorIdUseCase, ListarWorkspacesPorIdUsuarioUseCase listarWorkspacesPorIdUsuarioUseCase, ListarWorkspacesPorIdUsuarioENomeUseCase listarWorkspacesPorIdUsuarioENomeUseCase, AtualizarWorkspaceUseCase atualizarWorkspaceUseCase) {
        this.cadastrarWorkspaceUseCase = cadastrarWorkspaceUseCase;
        this.listarTodosWorkspacesUseCase = listarTodosWorkspacesUseCase;
        this.buscarWorkspacePorIdUseCase = buscarWorkspacePorIdUseCase;
        this.listarWorkspacesPorIdUsuarioUseCase = listarWorkspacesPorIdUsuarioUseCase;
        this.listarWorkspacesPorIdUsuarioENomeUseCase = listarWorkspacesPorIdUsuarioENomeUseCase;
        this.atualizarWorkspaceUseCase = atualizarWorkspaceUseCase;
    }

    @Operation(summary = "Cadastrar workspace",
            description = """
                Cria um novo workspace.

                Requer autenticação JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Workspace cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = WorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<WorkspaceResponseDTO> cadastrarWorkspace(@RequestBody @Valid WorkspaceRequestDTO request,
                                                                   UriComponentsBuilder uriBuilder) {
        WorkspaceResponseDTO response = cadastrarWorkspaceUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/workspaces/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(summary = "Listar workspaces",
            description = """
                Retorna uma lista paginada de workspaces.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workspaces retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<WorkspaceResponseDTO>> listarTodosWorkspaces(@PageableDefault(size = 10) Pageable pageable) {
        Page<WorkspaceResponseDTO> workspaces = listarTodosWorkspacesUseCase.execute(pageable);
        return ResponseEntity.ok(workspaces);
    }

    @Operation(summary = "Buscar workspace por ID",
            description = """
                Retorna os dados de um workspace.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário do workspace solicitado.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workspace encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = WorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Workspace não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDTO> buscarCorPorId(@PathVariable Long id) {
        WorkspaceResponseDTO cor = buscarWorkspacePorIdUseCase.execute(id);
        return ResponseEntity.ok(cor);
    }

    @Operation(summary = "Listar workspaces por ID do usuário",
            description = """
                Retorna uma lista paginada de workspaces de um usuário.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário dos workspaces solicitados.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workspaces retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<WorkspaceResponseDTO>> listarWorkspacesPorIdUsuario(@PathVariable Long idUsuario,
                                                                                   @RequestParam(required = false) String nome,
                                                                                   @PageableDefault(size = 10) Pageable pageable) {
        Page<WorkspaceResponseDTO> workspaces;
        if (nome != null && !nome.isBlank()) {
            workspaces = listarWorkspacesPorIdUsuarioENomeUseCase.execute(idUsuario, nome, pageable);
        } else {
            workspaces = listarWorkspacesPorIdUsuarioUseCase.execute(idUsuario, pageable);
        }
        return ResponseEntity.ok(workspaces);
    }

    @Operation(
            summary = "Atualizar workspace",
            description = """
            Atualiza os dados de um workspace.

            Apenas o dono do workspace pode atualizar os dados,
            exceto administradores.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workspace atualizado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Workspace não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarWorkspace(@PathVariable Long id, @RequestBody @Valid WorkspaceRequestDTO request) {
        atualizarWorkspaceUseCase.execute(id, request);
        return ResponseEntity.noContent().build();
    }
}
