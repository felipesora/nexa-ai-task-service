package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.dto.workspace.WorkspaceRequestDTO;
import com.nexa.task.application.dto.workspace.WorkspaceResponseDTO;
import com.nexa.task.application.dto.workspace.WorkspaceUpdateDTO;
import com.nexa.task.application.usecase.tarefa.ListarTarefasPorIdWorkspaceUseCase;
import com.nexa.task.application.usecase.workspace.AtivarWorkspaceUseCase;
import com.nexa.task.application.usecase.workspace.AtualizarWorkspaceUseCase;
import com.nexa.task.application.usecase.workspace.BuscarWorkspacePorIdUseCase;
import com.nexa.task.application.usecase.workspace.CadastrarWorkspaceUseCase;
import com.nexa.task.application.usecase.workspace.DesativarWorkspaceUseCase;
import com.nexa.task.application.usecase.workspace.ListarTodosWorkspacesUseCase;
import com.nexa.task.application.usecase.workspace.ListarWorkspacesPorIdUsuarioENomeUseCase;
import com.nexa.task.application.usecase.workspace.ListarWorkspacesPorIdUsuarioUseCase;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/workspaces")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Workspaces", description = "Operacoes para gerenciamento dos workspaces.")
public class WorkspaceController {
    private final CadastrarWorkspaceUseCase cadastrarWorkspaceUseCase;
    private final ListarTodosWorkspacesUseCase listarTodosWorkspacesUseCase;
    private final BuscarWorkspacePorIdUseCase buscarWorkspacePorIdUseCase;
    private final ListarWorkspacesPorIdUsuarioUseCase listarWorkspacesPorIdUsuarioUseCase;
    private final ListarWorkspacesPorIdUsuarioENomeUseCase listarWorkspacesPorIdUsuarioENomeUseCase;
    private final ListarTarefasPorIdWorkspaceUseCase listarTarefasPorIdWorkspaceUseCase;
    private final AtualizarWorkspaceUseCase atualizarWorkspaceUseCase;
    private final DesativarWorkspaceUseCase desativarWorkspaceUseCase;
    private final AtivarWorkspaceUseCase ativarWorkspaceUseCase;

    public WorkspaceController(
            CadastrarWorkspaceUseCase cadastrarWorkspaceUseCase,
            ListarTodosWorkspacesUseCase listarTodosWorkspacesUseCase,
            BuscarWorkspacePorIdUseCase buscarWorkspacePorIdUseCase,
            ListarWorkspacesPorIdUsuarioUseCase listarWorkspacesPorIdUsuarioUseCase,
            ListarWorkspacesPorIdUsuarioENomeUseCase listarWorkspacesPorIdUsuarioENomeUseCase,
            ListarTarefasPorIdWorkspaceUseCase listarTarefasPorIdWorkspaceUseCase,
            AtualizarWorkspaceUseCase atualizarWorkspaceUseCase,
            DesativarWorkspaceUseCase desativarWorkspaceUseCase,
            AtivarWorkspaceUseCase ativarWorkspaceUseCase) {
        this.cadastrarWorkspaceUseCase = cadastrarWorkspaceUseCase;
        this.listarTodosWorkspacesUseCase = listarTodosWorkspacesUseCase;
        this.buscarWorkspacePorIdUseCase = buscarWorkspacePorIdUseCase;
        this.listarWorkspacesPorIdUsuarioUseCase = listarWorkspacesPorIdUsuarioUseCase;
        this.listarWorkspacesPorIdUsuarioENomeUseCase = listarWorkspacesPorIdUsuarioENomeUseCase;
        this.listarTarefasPorIdWorkspaceUseCase = listarTarefasPorIdWorkspaceUseCase;
        this.atualizarWorkspaceUseCase = atualizarWorkspaceUseCase;
        this.desativarWorkspaceUseCase = desativarWorkspaceUseCase;
        this.ativarWorkspaceUseCase = ativarWorkspaceUseCase;
    }

    @Operation(
            summary = "Cadastrar workspace",
            description = """
                Cria um novo workspace.

                Regras de negocio:
                - O `id_usuario` informado deve pertencer ao usuario autenticado ou a um usuario administrado por ROLE_ADMIN.
                - O nome deve ter entre 3 e 150 caracteres.
                - A descricao, quando informada, deve ter entre 3 e 500 caracteres.
                - Nao pode existir outro workspace com o mesmo nome para o mesmo usuario.
                - `id_cor` e `id_icone` sao opcionais, mas quando informados precisam existir.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Workspace cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = WorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou nome de workspace ja utilizado por este usuario",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cor ou icone nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<WorkspaceResponseDTO> cadastrarWorkspace(
            @RequestBody @Valid WorkspaceRequestDTO request,
            UriComponentsBuilder uriBuilder) {
        WorkspaceResponseDTO response = cadastrarWorkspaceUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/workspaces/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(
            summary = "Listar workspaces",
            description = """
                Retorna uma lista paginada de workspaces.

                Regras de negocio:
                - Endpoint restrito a usuarios com ROLE_ADMIN.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workspaces retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<WorkspaceResponseDTO>> listarTodosWorkspaces(@PageableDefault(size = 10) Pageable pageable) {
        Page<WorkspaceResponseDTO> workspaces = listarTodosWorkspacesUseCase.execute(pageable);
        return ResponseEntity.ok(workspaces);
    }

    @Operation(
            summary = "Buscar workspace por ID",
            description = """
                Retorna os dados de um workspace.

                Regras de negocio:
                - O workspace deve existir.
                - O acesso e permitido para administradores ou para o proprietario do workspace.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workspace encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = WorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Workspace nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDTO> buscarWorkspacePorId(
            @Parameter(description = "Identificador do workspace") @PathVariable Long id) {
        WorkspaceResponseDTO workspace = buscarWorkspacePorIdUseCase.execute(id);
        return ResponseEntity.ok(workspace);
    }

    @Operation(
            summary = "Listar workspaces por usuario",
            description = """
                Retorna uma lista paginada dos workspaces de um usuario.

                Regras de negocio:
                - Apenas administradores ou o proprio usuario podem consultar a lista.
                - Quando o parametro `nome` e informado, o retorno e filtrado por usuario e nome.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workspaces retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<WorkspaceResponseDTO>> listarWorkspacesPorIdUsuario(
            @Parameter(description = "Identificador do usuario proprietario dos workspaces") @PathVariable Long idUsuario,
            @Parameter(description = "Filtro opcional por nome do workspace") @RequestParam(required = false) String nome,
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
            summary = "Listar tarefas por workspace",
            description = """
                Retorna uma lista paginada de tarefas vinculadas a um workspace.

                Regras de negocio:
                - O workspace deve existir.
                - Apenas administradores ou o proprietario do workspace podem consultar suas tarefas.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Workspace nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{idWorkspace}/tarefas")
    public ResponseEntity<Page<TarefaResponseDTO>> listarTarefasPeloWorkspace(
            @Parameter(description = "Identificador do workspace") @PathVariable Long idWorkspace,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<TarefaResponseDTO> tarefas = listarTarefasPorIdWorkspaceUseCase.execute(idWorkspace, pageable);
        return ResponseEntity.ok(tarefas);
    }

    @Operation(
            summary = "Atualizar workspace",
            description = """
                Atualiza os dados editaveis de um workspace.

                Regras de negocio:
                - O workspace deve existir.
                - Apenas o proprietario do workspace ou usuarios com ROLE_ADMIN podem atualizar.
                - O nome deve ter entre 3 e 150 caracteres.
                - A descricao, quando informada, deve ter entre 3 e 500 caracteres.
                - Nao pode existir outro workspace com o mesmo nome para o mesmo usuario.
                - `id_cor` e `id_icone` sao opcionais, mas quando informados precisam existir.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workspace atualizado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou nome de workspace ja utilizado por este usuario",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Workspace, cor ou icone nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarWorkspace(
            @Parameter(description = "Identificador do workspace") @PathVariable Long id,
            @RequestBody @Valid WorkspaceUpdateDTO request) {
        atualizarWorkspaceUseCase.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar workspace",
            description = """
                Realiza a desativacao logica de um workspace.

                Regras de negocio:
                - O workspace deve existir.
                - Apenas o proprietario do workspace ou usuarios com ROLE_ADMIN podem desativar.
                - A desativacao do workspace tambem desativa, de forma logica, todas as tarefas e subtarefas vinculadas.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workspace desativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Workspace nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarWorkspace(
            @Parameter(description = "Identificador do workspace") @PathVariable Long id) {
        desativarWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar workspace",
            description = """
                Reativa um workspace previamente desativado.

                Regras de negocio:
                - O workspace deve existir.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.
                - A operacao reativa apenas o workspace; as tarefas e subtarefas desativadas nao sao reativadas automaticamente por este endpoint.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workspace ativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Workspace nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarUsuario(
            @Parameter(description = "Identificador do workspace") @PathVariable Long id) {
        ativarWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
