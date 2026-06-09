package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.usecase.iconeWorkspace.CadastrarIconeWorkspaceUseCase;
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
@RequestMapping("/v1/icones-workspace")
@Tag(name = "Ícones de Workspace", description = "Operações para gerenciamento dos ícones utilizados nos workspaces.")
public class IconeWorkspaceController {

    private final CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase;

    public IconeWorkspaceController(CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase) {
        this.cadastrarIconeWorkspaceUseCase = cadastrarIconeWorkspaceUseCase;
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
            @ApiResponse(
                    responseCode = "201",
                    description = "Ícone cadastrado com sucesso",
                    content = @Content(
                            schema = @Schema(implementation = IconeWorkspaceResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuário sem permissão",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<IconeWorkspaceResponseDTO> cadastrarIcone(@RequestBody @Valid IconeWorkspaceRequestDTO request,
                                                                    UriComponentsBuilder uriBuilder) {
        IconeWorkspaceResponseDTO response = cadastrarIconeWorkspaceUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/icones-workspace/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }
}
