package com.nexa.task.application.dto.subtarefa;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({ "id_subtarefa", "titulo", "concluida", "criado_em", "atualizado_em",
        "ativo", "id_tarefa" })
public record SubtarefaResponseDTO(
        @JsonProperty("id_subtarefa")
        Long id,

        String titulo,

        Boolean concluida,

        @JsonProperty("criado_em")
        LocalDateTime criadoEm,

        @JsonProperty("atualizado_em")
        LocalDateTime atualizadoEm,

        Boolean ativo,

        @JsonProperty("id_tarefa")
        Long idTarefa
) {
}
