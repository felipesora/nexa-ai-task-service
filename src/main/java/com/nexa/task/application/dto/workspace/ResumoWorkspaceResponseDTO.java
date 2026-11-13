package com.nexa.task.application.dto.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "total", "concluidas", "em_andamento", "pendentes", "atrasadas" })
public record ResumoWorkspaceResponseDTO(
        Long total,

        Long concluidas,

        @JsonProperty("em_andamento")
        Long emAndamento,

        Long pendentes,

        Long atrasadas
) {
}
