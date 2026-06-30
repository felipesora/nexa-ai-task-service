package com.nexa.task.presentation.controller;

import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceRequestDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;
import com.nexa.task.application.usecase.iconeWorkspace.AtivarIconeWorkspaceUseCase;
import com.nexa.task.application.usecase.iconeWorkspace.BuscarIconeWorkspacePorIdUserCase;
import com.nexa.task.application.usecase.iconeWorkspace.CadastrarIconeWorkspaceUseCase;
import com.nexa.task.application.usecase.iconeWorkspace.DesativarIconeWorkspaceUseCase;
import com.nexa.task.application.usecase.iconeWorkspace.ListarTodosIconesWorkspaceUseCase;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/icones-workspace")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Icones de Workspace", description = "Operacoes para gerenciamento dos icones utilizados nos workspaces.")
public class IconeWorkspaceController {

    private final CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase;
    private final ListarTodosIconesWorkspaceUseCase listarTodosIconesWorkspaceUseCase;
    private final BuscarIconeWorkspacePorIdUserCase buscarIconeWorkspacePorIdUserCase;
    private final DesativarIconeWorkspaceUseCase desativarIconeWorkspaceUseCase;
    private final AtivarIconeWorkspaceUseCase ativarIconeWorkspaceUseCase;

    public IconeWorkspaceController(
            CadastrarIconeWorkspaceUseCase cadastrarIconeWorkspaceUseCase,
            ListarTodosIconesWorkspaceUseCase listarTodosIconesWorkspaceUseCase,
            BuscarIconeWorkspacePorIdUserCase buscarIconeWorkspacePorIdUserCase,
            DesativarIconeWorkspaceUseCase desativarIconeWorkspaceUseCase,
            AtivarIconeWorkspaceUseCase ativarIconeWorkspaceUseCase) {
        this.cadastrarIconeWorkspaceUseCase = cadastrarIconeWorkspaceUseCase;
        this.listarTodosIconesWorkspaceUseCase = listarTodosIconesWorkspaceUseCase;
        this.buscarIconeWorkspacePorIdUserCase = buscarIconeWorkspacePorIdUserCase;
        this.desativarIconeWorkspaceUseCase = desativarIconeWorkspaceUseCase;
        this.ativarIconeWorkspaceUseCase = ativarIconeWorkspaceUseCase;
    }

    @Operation(
            summary = "Cadastrar icone de workspace",
            description = """
                Cria um novo icone de workspace.

                Regras de negocio:
                - O nome deve ter entre 3 e 100 caracteres.
                - O caminho deve ter entre 3 e 255 caracteres.
                - O nome do icone deve ser unico no sistema.
                - O caminho do icone deve ser unico no sistema.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Icone de workspace cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = IconeWorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados invalidos, nome ja cadastrado ou caminho ja cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<IconeWorkspaceResponseDTO> cadastrarIcone(
            @RequestBody @Valid IconeWorkspaceRequestDTO request,
            UriComponentsBuilder uriBuilder) {
        IconeWorkspaceResponseDTO response = cadastrarIconeWorkspaceUseCase.execute(request);
        URI endereco = uriBuilder.path("/v1/icones-workspace/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(endereco).body(response);
    }

    @Operation(
            summary = "Listar icones de workspace",
            description = """
                Retorna uma lista paginada de icones de workspace.

                Regras de negocio:
                - O retorno contem os registros paginados do catalogo de icones de workspace.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Icones de workspace retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<IconeWorkspaceResponseDTO>> listarTodosIcones(@PageableDefault(size = 10) Pageable pageable) {
        Page<IconeWorkspaceResponseDTO> icones = listarTodosIconesWorkspaceUseCase.execute(pageable);
        return ResponseEntity.ok(icones);
    }

    @Operation(
            summary = "Buscar icone de workspace por ID",
            description = """
                Retorna os dados de um icone de workspace.

                Regras de negocio:
                - O icone de workspace deve existir.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Icone de workspace encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = IconeWorkspaceResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Icone de workspace nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<IconeWorkspaceResponseDTO> buscarIconePorId(
            @Parameter(description = "Identificador do icone de workspace") @PathVariable Long id) {
        IconeWorkspaceResponseDTO icone = buscarIconeWorkspacePorIdUserCase.execute(id);
        return ResponseEntity.ok(icone);
    }

    @Operation(
            summary = "Desativar icone de workspace",
            description = """
                Realiza a desativacao logica de um icone de workspace.

                Regras de negocio:
                - O icone de workspace deve existir.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Icone de workspace desativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Icone de workspace nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarIcone(
            @Parameter(description = "Identificador do icone de workspace") @PathVariable Long id) {
        desativarIconeWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Ativar icone de workspace",
            description = """
                Reativa um icone de workspace previamente desativado.

                Regras de negocio:
                - O icone de workspace deve existir.
                - Apenas usuarios com ROLE_ADMIN podem executar esta operacao.

                Requer autenticacao JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Icone de workspace ativado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuario nao autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Usuario sem permissao",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Icone de workspace nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> ativarIcone(
            @Parameter(description = "Identificador do icone de workspace") @PathVariable Long id) {
        ativarIconeWorkspaceUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
