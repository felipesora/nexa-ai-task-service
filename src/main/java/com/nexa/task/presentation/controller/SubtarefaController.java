package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.subtarefa.SubtarefaCreateDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaUpdateDTO;
import com.nexa.task.application.usecase.subtarefa.*;
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
@RequestMapping("/v1/subtarefas")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Subtarefas", description = "Operacoes para gerenciamento das subtarefas.")
public class SubtarefaController {

    private final CadastrarSubtarefaUseCase cadastrarSubtarefaUseCase;
    private final ListarTodasSubtarefasUseCase listarTodasSubtarefasUseCase;
    private final BuscarSubtarefaPorIdUseCase buscarSubtarefaPorIdUseCase;
    private final AtualizarSubtarefaUseCase atualizarSubtarefaUseCase;
    private final DesativarSubtarefaUseCase desativarSubtarefaUseCase;
    private final AtivarSubtarefaUseCase ativarSubtarefaUseCase;
    private final ConcluirSubtarefaUseCase concluirSubtarefaUseCase;
    private final DesmarcarSubtarefaConcluidaUseCase desmarcarSubtarefaConcluidaUseCase;

    public SubtarefaController(CadastrarSubtarefaUseCase cadastrarSubtarefaUseCase,
                               ListarTodasSubtarefasUseCase listarTodasSubtarefasUseCase,
                               BuscarSubtarefaPorIdUseCase buscarSubtarefaPorIdUseCase,
                               AtualizarSubtarefaUseCase atualizarSubtarefaUseCase,
                               DesativarSubtarefaUseCase desativarSubtarefaUseCase,
                               AtivarSubtarefaUseCase ativarSubtarefaUseCase,
                               ConcluirSubtarefaUseCase concluirSubtarefaUseCase,
                               DesmarcarSubtarefaConcluidaUseCase desmarcarSubtarefaConcluidaUseCase) {
        this.cadastrarSubtarefaUseCase = cadastrarSubtarefaUseCase;
        this.listarTodasSubtarefasUseCase = listarTodasSubtarefasUseCase;
        this.buscarSubtarefaPorIdUseCase = buscarSubtarefaPorIdUseCase;
        this.atualizarSubtarefaUseCase = atualizarSubtarefaUseCase;
        this.desativarSubtarefaUseCase = desativarSubtarefaUseCase;
        this.ativarSubtarefaUseCase = ativarSubtarefaUseCase;
        this.concluirSubtarefaUseCase = concluirSubtarefaUseCase;
        this.desmarcarSubtarefaConcluidaUseCase = desmarcarSubtarefaConcluidaUseCase;
    }

    @Operation(
            summary = "Cadastrar subtarefa",
            description = """
                Cria uma nova subtarefa vinculada a uma tarefa existente.

                Regras de negocio:
                - A tarefa informada em `id_tarefa` deve existir.
                - Apenas o proprietario da tarefa ou usuarios com ROLE_ADMIN podem cadastrar subtarefas nela.
                - O titulo deve ter entre 3 e 200 caracteres.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Subtarefa cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = SubtarefaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<SubtarefaResponseDTO> cadastrarSubtarefa(@RequestBody @Valid SubtarefaCreateDTO request,
                                                             UriComponentsBuilder uriBuilder) {
        SubtarefaResponseDTO response = cadastrarSubtarefaUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/subtarefas/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(
            summary = "Listar subtarefas",
            description = """
                Retorna uma lista paginada de subtarefas.

                Regras de negocio:
                - Endpoint restrito a usuarios com ROLE_ADMIN.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subtarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<SubtarefaResponseDTO>> listarTodasSubtarefas(@PageableDefault(size = 10) Pageable pageable) {
        Page<SubtarefaResponseDTO> subtarefas = listarTodasSubtarefasUseCase.execute(pageable);
        return ResponseEntity.ok(subtarefas);
    }

    @Operation(
            summary = "Buscar subtarefa por ID",
            description = """
                Retorna os dados de uma subtarefa.

                Regras de negocio:
                - A subtarefa deve existir.
                - O acesso e permitido para administradores ou para o proprietario da subtarefa.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subtarefa encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = SubtarefaResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<SubtarefaResponseDTO> buscarSubtarefaPorId(
            @Parameter(description = "Identificador da subtarefa") @PathVariable Long id) {
        SubtarefaResponseDTO subtarefa = buscarSubtarefaPorIdUseCase.execute(id);
        return ResponseEntity.ok(subtarefa);
    }

    @Operation(
            summary = "Atualizar subtarefa",
            description = """
            Atualiza os dados editaveis de uma subtarefa.

            Regras de negocio:
            - A subtarefa deve existir.
            - Apenas o proprietario da subtarefa ou usuarios com ROLE_ADMIN podem atualizar.
            - O titulo deve ter entre 3 e 200 caracteres.

            Requer autenticacao JWT.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subtarefa atualizada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarSubtarefa(
            @Parameter(description = "Identificador da subtarefa") @PathVariable Long id,
            @RequestBody @Valid SubtarefaUpdateDTO dto) {
        atualizarSubtarefaUseCase.execute(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar subtarefa",
            description = """
                Realiza a desativacao logica de uma subtarefa.

                Regras de negocio:
                - A subtarefa deve existir.
                - Apenas o proprietario da subtarefa ou usuarios com ROLE_ADMIN podem desativar.
                - A operacao realiza exclusao logica; a subtarefa nao e removida fisicamente.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subtarefa desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarSubtarefa(
            @Parameter(description = "Identificador da subtarefa") @PathVariable Long id) {
        desativarSubtarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar subtarefa",
            description = """
                Reativa uma subtarefa previamente desativada.

                Regras de negocio:
                - A subtarefa deve existir.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.
                - A operacao remove a marcacao de desativacao logica da subtarefa.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subtarefa ativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarSubtarefa(
            @Parameter(description = "Identificador da subtarefa") @PathVariable Long id) {
        ativarSubtarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Marcar subtarefa como concluida",
            description = """
                Marca uma subtarefa como concluida.

                Regras de negocio:
                - A subtarefa deve existir.
                - Apenas o proprietario da subtarefa ou usuarios com ROLE_ADMIN podem concluir.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subtarefa marcada como concluida com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluirSubtarefa(
            @Parameter(description = "Identificador da subtarefa") @PathVariable Long id) {
        concluirSubtarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desmarcar subtarefa como concluida",
            description = """
                Remove a marcacao de concluida de uma subtarefa.

                Regras de negocio:
                - A subtarefa deve existir.
                - Apenas o proprietario da subtarefa ou usuarios com ROLE_ADMIN podem executar a operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Marcacao de conclusao removida com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa nao encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/desmarcar-conclusao")
    public ResponseEntity<Void> desmarcarConclusaoSubtarefa(
            @Parameter(description = "Identificador da subtarefa") @PathVariable Long id) {
        desmarcarSubtarefaConcluidaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
