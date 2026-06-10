package com.nexa.task.application.dto.corTag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id_cor", "cor", "ativo" })
public record CorTagResponseDTO(
        @JsonProperty("id_cor")
        Long id,

        String cor,

        Boolean ativo
) {
}
