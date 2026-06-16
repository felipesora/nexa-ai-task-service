package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.subtarefa.SubtarefaCreateDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaUpdateDTO;
import com.nexa.task.application.usecase.subtarefa.*;
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
@RequestMapping("/v1/subtarefas")
@Tag(name = "Subtarefas", description = "Operações para gerenciamento das subtarefas.")
public class SubtarefaController {

    private final CadastrarSubtarefaUseCase cadastrarSubtarefaUseCase;
    private final ListarTodasSubtarefasUseCase listarTodasSubtarefasUseCase;
    private final ListarSubtarefasPorIdTarefaUseCase listarSubtarefasPorIdTarefaUseCase;
    private final BuscarSubtarefaPorIdUseCase buscarSubtarefaPorIdUseCase;
    private final AtualizarSubtarefaUseCase atualizarSubtarefaUseCase;
    private final DesativarSubtarefaUseCase desativarSubtarefaUseCase;
    private final AtivarSubtarefaUseCase ativarSubtarefaUseCase;

    public SubtarefaController(CadastrarSubtarefaUseCase cadastrarSubtarefaUseCase, ListarTodasSubtarefasUseCase listarTodasSubtarefasUseCase, ListarSubtarefasPorIdTarefaUseCase listarSubtarefasPorIdTarefaUseCase, BuscarSubtarefaPorIdUseCase buscarSubtarefaPorIdUseCase, AtualizarSubtarefaUseCase atualizarSubtarefaUseCase, DesativarSubtarefaUseCase desativarSubtarefaUseCase, AtivarSubtarefaUseCase ativarSubtarefaUseCase) {
        this.cadastrarSubtarefaUseCase = cadastrarSubtarefaUseCase;
        this.listarTodasSubtarefasUseCase = listarTodasSubtarefasUseCase;
        this.listarSubtarefasPorIdTarefaUseCase = listarSubtarefasPorIdTarefaUseCase;
        this.buscarSubtarefaPorIdUseCase = buscarSubtarefaPorIdUseCase;
        this.atualizarSubtarefaUseCase = atualizarSubtarefaUseCase;
        this.desativarSubtarefaUseCase = desativarSubtarefaUseCase;
        this.ativarSubtarefaUseCase = ativarSubtarefaUseCase;
    }

    @Operation(summary = "Cadastrar subtarefa",
            description = """
                Cria uma nova subtarefa.

                Requer autenticação JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Subtarefa cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = SubtarefaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<SubtarefaResponseDTO> cadastrarSubtarefa(@RequestBody @Valid SubtarefaCreateDTO request,
                                                             UriComponentsBuilder uriBuilder) {
        SubtarefaResponseDTO response = cadastrarSubtarefaUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/subtarefas/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(summary = "Listar subtarefas",
            description = """
                Retorna uma lista paginada de subtarefas.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subtarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<SubtarefaResponseDTO>> listarTodasSubtarefas(@PageableDefault(size = 10) Pageable pageable) {
        Page<SubtarefaResponseDTO> subtarefas = listarTodasSubtarefasUseCase.execute(pageable);
        return ResponseEntity.ok(subtarefas);
    }

    @Operation(summary = "Buscar subtarefa por ID",
            description = """
                Retorna os dados de uma subtarefa.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário da subtarefa solicitada.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subtarefa encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = SubtarefaResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubtarefaResponseDTO> buscarSubtarefaPorId(@PathVariable Long id) {
        SubtarefaResponseDTO subtarefa = buscarSubtarefaPorIdUseCase.execute(id);
        return ResponseEntity.ok(subtarefa);
    }

    @Operation(summary = "Listar subtarefas por ID da tarefa",
            description = """
                Retorna uma lista paginada de subtarefas de uma tarefa.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário das subtarefas solicitadas.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subtarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/tarefa/{idTarefa}")
    public ResponseEntity<Page<SubtarefaResponseDTO>> listarSubtarefasPorIdTarefa(@PathVariable Long idTarefa,
                                                                               @PageableDefault(size = 10) Pageable pageable) {
        Page<SubtarefaResponseDTO> subtarefas = listarSubtarefasPorIdTarefaUseCase.execute(idTarefa, pageable);
        return ResponseEntity.ok(subtarefas);
    }

    @Operation(
            summary = "Atualizar subtarefa",
            description = """
            Atualiza os dados de uma subtarefa.

            Apenas o dono da subtarefa pode atualizar os dados,
            exceto administradores.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subtarefa atualizada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarSubtarefa(@PathVariable Long id, @RequestBody @Valid SubtarefaUpdateDTO dto) {
        atualizarSubtarefaUseCase.execute(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desativar subtarefa",
            description = """
                Realiza a desativação lógica de uma subtarefa.

                Apenas o dono da subtarefa pode desativa-la,
                exceto administradores.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subtarefa desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarSubtarefa(@PathVariable Long id) {
        desativarSubtarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar subtarefa",
            description = """
                Reativa uma subtarefa previamente desativada.

                Apenas administradores podem executar esta operação.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subtarefa ativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Subtarefa não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarSubtarefa(@PathVariable Long id) {
        ativarSubtarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
