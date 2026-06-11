package com.nexa.task.application.dto.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexa.task.application.dto.corWorkspace.CorWorkspaceResponseDTO;
import com.nexa.task.application.dto.iconeWorkspace.IconeWorkspaceResponseDTO;

import java.time.LocalDateTime;

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
