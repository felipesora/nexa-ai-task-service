package com.nexa.task.application.dto.corWorkspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id_cor", "cor", "ativo" })
public record CorWorkspaceResponseDTO(
        @JsonProperty("id_cor")
        Long id,

        String cor,

        Boolean ativo
) {
}
