package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.usecase.iconeWorkspace.BuscarIconeWorkspacePorIdUserCase;
import com.nexa.task.application.usecase.iconeWorkspace.CadastrarIconeWorkspaceUseCase;
import com.nexa.task.application.usecase.iconeWorkspace.ListarTodosIconesWorkspaceUseCase;
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
@RequestMapping("/v1/icones-workspace")
@Tag(name = "Ícones de Workspace", description = "Operações para gerenciamento dos ícones utilizados nos workspaces.")
public class IconeWorkspaceController {

    private final CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase;
    private final ListarTodosIconesWorkspaceUseCase listarTodosIconesWorkspaceUseCase;
    private final BuscarIconeWorkspacePorIdUserCase buscarIconeWorkspacePorIdUserCase;

    public IconeWorkspaceController(CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase, ListarTodosIconesWorkspaceUseCase listarTodosIconesWorkspaceUseCase, BuscarIconeWorkspacePorIdUserCase buscarIconeWorkspacePorIdUserCase) {
        this.cadastrarIconeWorkspaceUseCase = cadastrarIconeWorkspaceUseCase;
        this.listarTodosIconesWorkspaceUseCase = listarTodosIconesWorkspaceUseCase;
        this.buscarIconeWorkspacePorIdUserCase = buscarIconeWorkspacePorIdUserCase;
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
            @ApiResponse(responseCode = "201", description = "Ícone cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = IconeWorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<IconeWorkspaceResponseDTO> cadastrarIcone(@RequestBody @Valid IconeWorkspaceRequestDTO request,
                                                                    UriComponentsBuilder uriBuilder) {
        IconeWorkspaceResponseDTO response = cadastrarIconeWorkspaceUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/icones-workspace/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(summary = "Listar ícones de workspace",
            description = """
                Retorna uma lista paginada de ícones de workspace.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ícones retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<IconeWorkspaceResponseDTO>> listarTodosIcones(@PageableDefault(size = 10)Pageable pageable) {
        Page<IconeWorkspaceResponseDTO> icones = listarTodosIconesWorkspaceUseCase.execute(pageable);
        return ResponseEntity.ok(icones);
    }

    @Operation(summary = "Buscar ícone de workspace por ID",
            description = """
                Retorna os dados de um ícone de workspace.

                Requer autenticação JWT.
                Apenas usuários com ROLE_ADMIN podem acessar.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ícone encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ícone não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<IconeWorkspaceResponseDTO> buscarIconePorId(@PathVariable Long id) {
        IconeWorkspaceResponseDTO icone = buscarIconeWorkspacePorIdUserCase.execute(id);
        return ResponseEntity.ok(icone);
    }
}
