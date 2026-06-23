package com.nexa.task.application.dto.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nexa.task.application.dto.corTag.CorTagResponseDTO;

@JsonPropertyOrder({ "id_tag", "id_usuario", "nome", "ativo", "cor_tag" })
public record TagResponseDTO(
        @JsonProperty("id_tag")
        Long id,

        @JsonProperty("id_usuario")
        Long idUsuario,

        String nome,

        Boolean ativo,

        @JsonProperty("cor_tag")
        CorTagResponseDTO corTag
) {
}
