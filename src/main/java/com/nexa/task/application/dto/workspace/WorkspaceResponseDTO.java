package com.nexa.task.application.dto.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;

import java.time.LocalDateTime;

@JsonPropertyOrder({ "id_workspace", "id_usuario", "nome", "descricao", "criado_em", "atualizado_em", "ativo", "cor_workspace", "icone_workspace" })
public record WorkspaceResponseDTO(
        @JsonProperty("id_workspace")
        Long id,

        @JsonProperty("id_usuario")
        Long idUsuario,

        String nome,

        String descricao,

        @JsonProperty("criado_em")
        LocalDateTime criadoEm,

        @JsonProperty("atualizado_em")
        LocalDateTime atualizadoEm,

        Boolean ativo,

        @JsonProperty("cor_workspace")
        CorWorkspaceResponseDTO corWorkspace,

        @JsonProperty("icone_workspace")
        IconeWorkspaceResponseDTO iconeWorkspace
) {
}
