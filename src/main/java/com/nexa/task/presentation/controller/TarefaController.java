package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.dto.tag.TagResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaCreateDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.dto.tarefa.TarefaUpdateDTO;
import com.nexa.task.application.usecase.subtarefa.ListarSubtarefasPorIdTarefaUseCase;
import com.nexa.task.application.usecase.tag.ListarTagsPorIdTarefaUseCase;
import com.nexa.task.application.usecase.tarefa.AdicionarTagNaTarefaUseCase;
import com.nexa.task.application.usecase.tarefa.AtivarTarefaUseCase;
import com.nexa.task.application.usecase.tarefa.AtualizarTarefaUseCase;
import com.nexa.task.application.usecase.tarefa.BuscarTarefaPorIdUseCase;
import com.nexa.task.application.usecase.tarefa.CadastrarTarefaUseCase;
import com.nexa.task.application.usecase.tarefa.ConcluirTarefaUseCase;
import com.nexa.task.application.usecase.tarefa.DesativarTarefaUseCase;
import com.nexa.task.application.usecase.tarefa.IniciarTarefaUseCase;
import com.nexa.task.application.usecase.tarefa.ListarTarefasPorIdUsuarioETituloUseCase;
import com.nexa.task.application.usecase.tarefa.ListarTarefasPorIdUsuarioUseCase;
import com.nexa.task.application.usecase.tarefa.ListarTodasTarefasUseCase;
import com.nexa.task.application.usecase.tarefa.ReabrirTarefaUseCase;
import com.nexa.task.application.usecase.tarefa.RemoverTagDaTarefaUseCase;
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
@RequestMapping("/v1/tarefas")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tarefas", description = "Operacoes para gerenciamento das tarefas.")
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

    public TarefaController(
            CadastrarTarefaUseCase cadastrarTarefaUseCase,
            ListarTodasTarefasUseCase listarTodasTarefasUseCase,
            ListarTarefasPorIdUsuarioUseCase listarTarefasPorIdUsuarioUseCase,
            ListarTarefasPorIdUsuarioETituloUseCase listarTarefasPorIdUsuarioETituloUseCase,
            ListarSubtarefasPorIdTarefaUseCase listarSubtarefasPorIdTarefaUseCase,
            ListarTagsPorIdTarefaUseCase listarTagsPorIdTarefaUseCase,
            BuscarTarefaPorIdUseCase buscarTarefaPorIdUseCase,
            AtualizarTarefaUseCase atualizarTarefaUseCase,
            ConcluirTarefaUseCase concluirTarefaUseCase,
            IniciarTarefaUseCase iniciarTarefaUseCase,
            ReabrirTarefaUseCase reabrirTarefaUseCase,
            DesativarTarefaUseCase desativarTarefaUseCase,
            AtivarTarefaUseCase ativarTarefaUseCase,
            AdicionarTagNaTarefaUseCase adicionarTagNaTarefaUseCase,
            RemoverTagDaTarefaUseCase removerTagDaTarefaUseCase) {
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

    @Operation(
            summary = "Cadastrar tarefa",
            description = """
                Cria uma nova tarefa dentro de um workspace existente.

                Regras de negocio:
                - O workspace informado em `id_workspace` deve existir.
                - Apenas o dono do workspace ou usuarios com ROLE_ADMIN podem cadastrar tarefas nele.
                - O titulo deve ter entre 3 e 200 caracteres.
                - A descricao, quando informada, deve ter entre 3 e 700 caracteres.
                - A data limite, quando informada, deve ser igual ou posterior ao momento atual.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TarefaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Workspace nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TarefaResponseDTO> cadastrarTarefa(
            @RequestBody @Valid TarefaCreateDTO request,
            UriComponentsBuilder uriBuilder) {
        TarefaResponseDTO response = cadastrarTarefaUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/tarefas/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(
            summary = "Listar tarefas",
            description = """
                Retorna uma lista paginada de tarefas.

                Regras de negocio:
                - Endpoint restrito a usuarios com ROLE_ADMIN.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<TarefaResponseDTO>> listarTodasTarefas(@PageableDefault(size = 10) Pageable pageable) {
        Page<TarefaResponseDTO> tarefas = listarTodasTarefasUseCase.execute(pageable);
        return ResponseEntity.ok(tarefas);
    }

    @Operation(
            summary = "Buscar tarefa por ID",
            description = """
                Retorna os dados de uma tarefa.

                Regras de negocio:
                - A tarefa deve existir.
                - O acesso e permitido para administradores ou para o proprietario da tarefa.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = TarefaResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> buscarTarefaPorId(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long id) {
        TarefaResponseDTO tarefa = buscarTarefaPorIdUseCase.execute(id);
        return ResponseEntity.ok(tarefa);
    }

    @Operation(
            summary = "Listar subtarefas por tarefa",
            description = """
                Retorna uma lista paginada de subtarefas vinculadas a tarefa informada.

                Regras de negocio:
                - A tarefa deve existir.
                - Apenas administradores ou o proprietario da tarefa podem consultar suas subtarefas.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subtarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{idTarefa}/subtarefas")
    public ResponseEntity<Page<SubtarefaResponseDTO>> listarSubtarefasPelaTarefa(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long idTarefa,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<SubtarefaResponseDTO> subtarefas = listarSubtarefasPorIdTarefaUseCase.execute(idTarefa, pageable);
        return ResponseEntity.ok(subtarefas);
    }

    @Operation(
            summary = "Listar tags por tarefa",
            description = """
                Retorna uma lista paginada de tags vinculadas a tarefa informada.

                Regras de negocio:
                - A tarefa deve existir.
                - Apenas administradores ou o proprietario da tarefa podem consultar suas tags.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tags retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{idTarefa}/tags")
    public ResponseEntity<Page<TagResponseDTO>> listarTagsPelaTarefa(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long idTarefa,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<TagResponseDTO> tags = listarTagsPorIdTarefaUseCase.execute(idTarefa, pageable);
        return ResponseEntity.ok(tags);
    }

    @Operation(
            summary = "Adicionar tag em uma tarefa",
            description = """
                Associa uma tag existente a uma tarefa.

                Regras de negocio:
                - A tarefa deve existir.
                - A tag deve existir.
                - Apenas administradores ou o proprietario da tarefa podem executar a operacao.
                - A tag deve pertencer ao mesmo usuario da tarefa.
                - A mesma tag nao pode ser vinculada duas vezes a uma mesma tarefa.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag adicionada a tarefa com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Tag ja vinculada a tarefa, tag de outro usuario ou dados invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa ou tag nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{idTarefa}/tags/{idTag}")
    public ResponseEntity<Void> adicionarTag(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long idTarefa,
            @Parameter(description = "Identificador da tag") @PathVariable Long idTag) {
        adicionarTagNaTarefaUseCase.execute(idTarefa, idTag);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remover tag de uma tarefa",
            description = """
                Remove a associacao de uma tag de uma tarefa.

                Regras de negocio:
                - A tarefa deve existir.
                - A tag deve existir.
                - Apenas administradores ou o proprietario da tarefa podem executar a operacao.
                - A tag deve pertencer ao mesmo usuario da tarefa.
                - A tag precisa estar atualmente vinculada a tarefa.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tag removida da tarefa com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Tag nao vinculada a tarefa, tag de outro usuario ou dados invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa ou tag nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{idTarefa}/tags/{idTag}")
    public ResponseEntity<Void> removerTag(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long idTarefa,
            @Parameter(description = "Identificador da tag") @PathVariable Long idTag) {
        removerTagDaTarefaUseCase.execute(idTarefa, idTag);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar tarefas por usuario",
            description = """
                Retorna uma lista paginada das tarefas de um usuario.

                Regras de negocio:
                - Apenas administradores ou o proprio usuario podem consultar a lista.
                - Quando o parametro `titulo` e informado, o retorno e filtrado por usuario e titulo.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<TarefaResponseDTO>> listarTarefasPorIdUsuario(
            @Parameter(description = "Identificador do usuario proprietario das tarefas") @PathVariable Long idUsuario,
            @Parameter(description = "Filtro opcional por titulo da tarefa") @RequestParam(required = false) String titulo,
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
                Atualiza os dados editaveis de uma tarefa.

                Regras de negocio:
                - A tarefa deve existir.
                - Apenas o proprietario da tarefa ou usuarios com ROLE_ADMIN podem atualizar.
                - O titulo deve ter entre 3 e 200 caracteres.
                - A descricao, quando informada, deve ter entre 3 e 700 caracteres.
                - A prioridade e obrigatoria.
                - A dificuldade e obrigatoria.
                - A data limite, quando informada, deve ser igual ou posterior ao momento atual.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa atualizada com sucesso", content = @Content),
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
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarTarefa(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long id,
            @RequestBody @Valid TarefaUpdateDTO dto) {
        atualizarTarefaUseCase.execute(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Concluir tarefa",
            description = """
                Altera o status da tarefa para CONCLUIDA e registra a data de conclusao.

                Regras de negocio:
                - A tarefa deve existir.
                - Apenas o proprietario da tarefa ou usuarios com ROLE_ADMIN podem concluir.
                - Uma tarefa ja concluida nao pode ser concluida novamente.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa concluida com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "A tarefa ja esta concluida ou nao pode ser concluida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluirTarefa(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long id) {
        concluirTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Iniciar tarefa",
            description = """
                Altera o status da tarefa para EM_ANDAMENTO.

                Regras de negocio:
                - A tarefa deve existir.
                - Apenas o proprietario da tarefa ou usuarios com ROLE_ADMIN podem iniciar.
                - Uma tarefa em andamento nao pode ser iniciada novamente.
                - Uma tarefa concluida precisa ser reaberta antes de voltar para EM_ANDAMENTO.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa iniciada com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "A tarefa ja esta em andamento, esta concluida ou nao pode ser iniciada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciarTarefa(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long id) {
        iniciarTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Reabrir tarefa",
            description = """
                Reabre uma tarefa concluida, alterando seu status para PENDENTE e removendo a data de conclusao.

                Regras de negocio:
                - A tarefa deve existir.
                - Apenas o proprietario da tarefa ou usuarios com ROLE_ADMIN podem reabrir.
                - Somente tarefas com status CONCLUIDA podem ser reabertas.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa reaberta com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "A tarefa nao esta concluida ou nao pode ser reaberta",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<Void> reabrirTarefa(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long id) {
        reabrirTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Desativar tarefa",
            description = """
                Realiza a desativacao logica de uma tarefa.

                Regras de negocio:
                - A tarefa deve existir.
                - Apenas o proprietario da tarefa ou usuarios com ROLE_ADMIN podem desativar.
                - A operacao realiza exclusao logica; a tarefa nao e removida fisicamente.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa desativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarTarefa(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long id) {
        desativarTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar tarefa",
            description = """
                Reativa uma tarefa previamente desativada.

                Regras de negocio:
                - A tarefa deve existir.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.
                - A operacao remove a marcacao de desativacao logica da tarefa.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa ativada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarTarefa(
            @Parameter(description = "Identificador da tarefa") @PathVariable Long id) {
        ativarTarefaUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
