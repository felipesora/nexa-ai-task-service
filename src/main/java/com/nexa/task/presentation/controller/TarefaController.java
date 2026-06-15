package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.tarefa.TarefaRequestDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.usecase.tarefa.*;
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
@RequestMapping("/v1/tarefas")
@Tag(name = "Tarefas", description = "Operações para gerenciamento das tarefas.")
public class TarefaController {

    private final CadastrarTarefaUseCase cadastrarTarefaUseCase;
    private final ListarTodasTarefasUseCase listarTodasTarefasUseCase;
    private final ListarTarefasPorIdWorkspaceUseCase listarTarefasPorIdWorkspaceUseCase;
    private final ListarTarefasPorIdUsuarioUseCase listarTarefasPorIdUsuarioUseCase;
    private final ListarTarefasPorIdUsuarioETituloUseCase listarTarefasPorIdUsuarioETituloUseCase;
    private final BuscarTarefaPorIdUseCase buscarTarefaPorIdUseCase;

    public TarefaController(CadastrarTarefaUseCase cadastrarTarefaUseCase, ListarTodasTarefasUseCase listarTodasTarefasUseCase, ListarTarefasPorIdWorkspaceUseCase listarTarefasPorIdWorkspaceUseCase, ListarTarefasPorIdUsuarioUseCase listarTarefasPorIdUsuarioUseCase, ListarTarefasPorIdUsuarioETituloUseCase listarTarefasPorIdUsuarioETituloUseCase, BuscarTarefaPorIdUseCase buscarTarefaPorIdUseCase) {
        this.cadastrarTarefaUseCase = cadastrarTarefaUseCase;
        this.listarTodasTarefasUseCase = listarTodasTarefasUseCase;
        this.listarTarefasPorIdWorkspaceUseCase = listarTarefasPorIdWorkspaceUseCase;
        this.listarTarefasPorIdUsuarioUseCase = listarTarefasPorIdUsuarioUseCase;
        this.listarTarefasPorIdUsuarioETituloUseCase = listarTarefasPorIdUsuarioETituloUseCase;
        this.buscarTarefaPorIdUseCase = buscarTarefaPorIdUseCase;
    }

    @Operation(summary = "Cadastrar tarefa",
            description = """
                Cria uma nova tarefa.

                Requer autenticação JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TarefaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<TarefaResponseDTO> cadastrarTarefa(@RequestBody @Valid TarefaRequestDTO request,
                                                                   UriComponentsBuilder uriBuilder) {
        TarefaResponseDTO response = cadastrarTarefaUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/tarefas/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(summary = "Listar tarefas",
            description = """
                Retorna uma lista paginada de tarefas.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<TarefaResponseDTO>> listarTodasTarefas(@PageableDefault(size = 10) Pageable pageable) {
        Page<TarefaResponseDTO> tarefas = listarTodasTarefasUseCase.execute(pageable);
        return ResponseEntity.ok(tarefas);
    }

    @Operation(summary = "Buscar tarefa por ID",
            description = """
                Retorna os dados de uma tarefa.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário da tarefa solicitada.
                """
    )
    @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Tarefa encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TarefaResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> buscarTarefaPorId(@PathVariable Long id) {
        TarefaResponseDTO tarefa = buscarTarefaPorIdUseCase.execute(id);
        return ResponseEntity.ok(tarefa);
    }

    @Operation(summary = "Listar tarefas por ID do workspace",
            description = """
                Retorna uma lista paginada de tarefas de um workspace.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário das tarefas solicitadas.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/workspace/{idWorkspace}")
    public ResponseEntity<Page<TarefaResponseDTO>> listarTarefasPorIdWorkspace(@PathVariable Long idWorkspace,
                                                                             @PageableDefault(size = 10) Pageable pageable) {
        Page<TarefaResponseDTO> tarefas = listarTarefasPorIdWorkspaceUseCase.execute(idWorkspace, pageable);
        return ResponseEntity.ok(tarefas);
    }

    @Operation(summary = "Listar tarefas por ID do usuário",
            description = """
                Retorna uma lista paginada de tarefas de um usuário.
                Pode-se filtrar tarefas pelo título.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário das tarefas solicitadas.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<TarefaResponseDTO>> listarTarefasPorIdUsuario(@PathVariable Long idUsuario,
                                                                                   @RequestParam(required = false) String titulo,
                                                                                   @PageableDefault(size = 10) Pageable pageable) {
        Page<TarefaResponseDTO> tarefas;
        if (titulo != null && !titulo.isBlank()) {
            tarefas = listarTarefasPorIdUsuarioETituloUseCase.execute(idUsuario, titulo, pageable);
        } else {
            tarefas = listarTarefasPorIdUsuarioUseCase.execute(idUsuario, pageable);
        }
        return ResponseEntity.ok(tarefas);
    }
}
