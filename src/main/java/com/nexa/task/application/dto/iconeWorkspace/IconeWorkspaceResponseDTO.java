package com.nexa.task.application.dto.iconeWorkspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id_icone", "nome", "caminho", "ativo" })
public record IconeWorkspaceResponseDTO(
        @JsonProperty("id_icone")
        Long id,

        String nome,

        String caminho,

        Boolean ativo
) {
}
