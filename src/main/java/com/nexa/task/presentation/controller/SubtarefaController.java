package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.subtarefa.SubtarefaRequestDTO;
import com.nexa.task.application.dto.subtarefa.SubtarefaResponseDTO;
import com.nexa.task.application.usecase.subtarefa.CadastrarSubtarefaUseCase;
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
@RequestMapping("/v1/subtarefas")
@Tag(name = "Subtarefas", description = "Operações para gerenciamento das subtarefas.")
public class SubtarefaController {

    private final CadastrarSubtarefaUseCase cadastrarSubtarefaUseCase;

    public SubtarefaController(CadastrarSubtarefaUseCase cadastrarSubtarefaUseCase) {
        this.cadastrarSubtarefaUseCase = cadastrarSubtarefaUseCase;
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
    public ResponseEntity<SubtarefaResponseDTO> cadastrarSubtarefa(@RequestBody @Valid SubtarefaRequestDTO request,
                                                             UriComponentsBuilder uriBuilder) {
        SubtarefaResponseDTO response = cadastrarSubtarefaUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/subtarefas/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }
}
