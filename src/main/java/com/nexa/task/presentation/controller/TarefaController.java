package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaCreateDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaUpdateDTO;
import com.nexa.task.application.usecase.subtarefa.ListarSubtarefasPorIdTarefaUseCase;
import com.nexa.task.application.usecase.tag.ListarTagsPorIdTarefaUseCase;
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
    private final ListarTarefasPorIdUsuarioUseCase listarTarefasPorIdUsuarioUseCase;
    private final ListarTarefasPorIdUsuarioETituloUseCase listarTarefasPorIdUsuarioETituloUseCase;
    private final ListarSubtarefasPorIdTarefaUseCase listarSubtarefasPorIdTarefaUseCase;
    private final ListarTagsPorIdTarefaUseCase listarTagsPorIdTarefaUseCase;
    private final BuscarTarefaPorIdUseCase buscarTarefaPorIdUseCase;
    private final AtualizarTarefaUseCase atualizarTarefaUseCase;
    private final ConcluirTarefaUseCase concluirTarefaUseCase;
    private final IniciarTarefaUseCase iniciarTarefaUseCase;
    private final ReabrirTarefaUseCase reabrirTarefaUseCase;
    private final DesativarTarefaUseCase desativarTarefaUseCase;
    private final AtivarTarefaUseCase ativarTarefaUseCase;
    private final AdicionarTagNaTarefaUseCase adicionarTagNaTarefaUseCase;
    private final RemoverTagDaTarefaUseCase removerTagDaTarefaUseCase;

    public TarefaController(CadastrarTarefaUseCase cadastrarTarefaUseCase, ListarTodasTarefasUseCase listarTodasTarefasUseCase, ListarTarefasPorIdUsuarioUseCase listarTarefasPorIdUsuarioUseCase, ListarTarefasPorIdUsuarioETituloUseCase listarTarefasPorIdUsuarioETituloUseCase, ListarSubtarefasPorIdTarefaUseCase listarSubtarefasPorIdTarefaUseCase, ListarTagsPorIdTarefaUseCase listarTagsPorIdTarefaUseCase, BuscarTarefaPorIdUseCase buscarTarefaPorIdUseCase, AtualizarTarefaUseCase atualizarTarefaUseCase, ConcluirTarefaUseCase concluirTarefaUseCase, IniciarTarefaUseCase iniciarTarefaUseCase, ReabrirTarefaUseCase reabrirTarefaUseCase, DesativarTarefaUseCase desativarTarefaUseCase, AtivarTarefaUseCase ativarTarefaUseCase, AdicionarTagNaTarefaUseCase adicionarTagNaTarefaUseCase, RemoverTagDaTarefaUseCase removerTagDaTarefaUseCase) {
        this.cadastrarTarefaUseCase = cadastrarTarefaUseCase;
        this.listarTodasTarefasUseCase = listarTodasTarefasUseCase;
        this.listarTarefasPorIdUsuarioUseCase = listarTarefasPorIdUsuarioUseCase;
        this.listarTarefasPorIdUsuarioETituloUseCase = listarTarefasPorIdUsuarioETituloUseCase;
        this.listarSubtarefasPorIdTarefaUseCase = listarSubtarefasPorIdTarefaUseCase;
        this.listarTagsPorIdTarefaUseCase = listarTagsPorIdTarefaUseCase;
        this.buscarTarefaPorIdUseCase = buscarTarefaPorIdUseCase;
        this.atualizarTarefaUseCase = atualizarTarefaUseCase;
        this.concluirTarefaUseCase = concluirTarefaUseCase;
        this.iniciarTarefaUseCase = iniciarTarefaUseCase;
        this.reabrirTarefaUseCase = reabrirTarefaUseCase;
        this.desativarTarefaUseCase = desativarTarefaUseCase;
        this.ativarTarefaUseCase = ativarTarefaUseCase;
        this.adicionarTagNaTarefaUseCase = adicionarTagNaTarefaUseCase;
        this.removerTagDaTarefaUseCase = removerTagDaTarefaUseCase;
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
    public ResponseEntity<TarefaResponseDTO> cadastrarTarefa(@RequestBody @Valid TarefaCreateDTO request,
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

    @Operation(summary = "Listar subtarefas por ID de uma tarefa",
            description = """
                Retorna uma lista paginada de subtarefas de uma tarefa.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário da tarefa solicitada.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subtarefas retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idTarefa}/subtarefas")
    public ResponseEntity<Page<SubtarefaResponseDTO>> listarSubtarefasPelaTarefa(@PathVariable Long idTarefa,
                                                                                 @PageableDefault(size = 10) Pageable pageable) {
        Page<SubtarefaResponseDTO> subtarefas = listarSubtarefasPorIdTarefaUseCase.execute(idTarefa, pageable);
        return ResponseEntity.ok(subtarefas);
    }

    @Operation(summary = "Listar tags por ID de uma tarefa",
            description = """
                Retorna uma lista paginada de tags de uma tarefa.

                Requer autenticação JWT.
                O acesso é permitido para:
                - Usuários com ROLE_ADMIN.
                - O proprietário da tarefa solicitada.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{idTarefa}/tags")
    public ResponseEntity<Page<TagResponseDTO>> listarTagsPelaTarefa(@PathVariable Long idTarefa,
                                                                           @PageableDefault(size = 10) Pageable pageable) {
        Page<TagResponseDTO> tags = listarTagsPorIdTarefaUseCase.execute(idTarefa, pageable);
        return ResponseEntity.ok(tags);
    }

    @Operation(
            summary = "Adicionar tag em uma tarefa",
            description = """
        Associa uma tag existente a uma tarefa.

        Requer autenticação JWT.

        O acesso é permitido para:
        - Usuários com ROLE_ADMIN.
        - O proprietário da tarefa.
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag adicionada à tarefa com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "A tag já está associada à tarefa ou os dados são inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa ou tag não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{idTarefa}/tags/{idTag}")
    public ResponseEntity<Void> adicionarTag(@PathVariable Long idTarefa, @PathVariable Long idTag) {
        adicionarTagNaTarefaUseCase.execute(idTarefa, idTag);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remover tag de uma tarefa",
            description = """
        Remove a associação de uma tag de uma tarefa.

        Requer autenticação JWT.

        O acesso é permitido para:
        - Usuários com ROLE_ADMIN.
        - O proprietário da tarefa.
        """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag removida da tarefa com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "A tag não está associada à tarefa",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa ou tag não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{idTarefa}/tags/{idTag}")
    public ResponseEntity<Void> removerTag(@PathVariable Long idTarefa, @PathVariable Long idTag) {
        removerTagDaTarefaUseCase.execute(idTarefa, idTag);
        return ResponseEntity.noContent().build();
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

    @Operation(
            summary = "Atualizar tarefa",
            description = """
            Atualiza os dados de uma tarefa.

            Apenas o dono da tarefa pode atualizar os dados,
            exceto administradores.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa atualizada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarTarefa(@PathVariable Long id, @RequestBody @Valid TarefaUpdateDTO dto) {
        atualizarTarefaUseCase.execute(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Concluir uma tarefa",
            description = """
            Altera o status da tarefa para CONCLUIDA e registra a data de conclusão.

            Requer autenticação JWT.
            Apenas o proprietário da tarefa ou usuários com ROLE_ADMIN
            podem executar esta operação.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa concluída com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "A tarefa já está concluída ou não pode ser concluída", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluirTarefa(@PathVariable Long id) {
        concluirTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Iniciar uma tarefa",
            description = """
            Altera o status da tarefa para EM_ANDAMENTO.
            
            Requer autenticação JWT.
    
            Apenas o proprietário da tarefa ou usuários com ROLE_ADMIN
            podem executar esta operação.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa iniciada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "A tarefa já está em andamento ou não pode ser iniciada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciarTarefa(@PathVariable Long id) {
        iniciarTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Reabrir tarefa",
            description = """
            Reabre uma tarefa concluída, alterando seu status para PENDENTE
            e removendo a data de conclusão.
    
            Requer autenticação JWT.
    
            Apenas o proprietário da tarefa ou usuários com ROLE_ADMIN
            podem executar esta operação.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa iniciada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "A tarefa não está concluída ou não pode ser reaberta", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<Void> reabrirTarefa(@PathVariable Long id) {
        reabrirTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar tarefa",
            description = """
            Realiza a desativação lógica de uma tarefa.

            Apenas o dono da tarefa pode desativa-la,
            exceto administradores.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarTarefa(@PathVariable Long id) {
        desativarTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar tarefa",
            description = """
            Reativa uma tarefa previamente desativada.

            Apenas administradores podem executar esta operaÃ§Ã£o.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa ativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarTarefa(@PathVariable Long id) {
        ativarTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }


}
