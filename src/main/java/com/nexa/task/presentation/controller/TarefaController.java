package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.tarefa.TarefaRequestDTO;
import com.nexa.task.application.dto.tarefa.TarefaResponseDTO;
import com.nexa.task.application.usecase.tarefa.CadastrarTarefaUseCase;
import com.nexa.task.presentation.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/tarefas")
@Tag(name = "Tarefas", description = "Operações para gerenciamento das tarefas.")
public class TarefaController {

    private final CadastrarTarefaUseCase cadastrarTarefaUseCase;

    public TarefaController(CadastrarTarefaUseCase cadastrarTarefaUseCase) {
        this.cadastrarTarefaUseCase = cadastrarTarefaUseCase;
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
}
