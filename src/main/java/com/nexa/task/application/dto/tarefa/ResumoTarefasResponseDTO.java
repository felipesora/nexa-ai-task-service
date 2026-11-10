package com.nexa.task.application.dto.tarefa;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "concluidas", "em_andamento", "pendentes", "atrasadas", "tarefas_prioritarias" })
public record ResumoTarefasResponseDTO(
        Long concluidas,

        @JsonProperty("em_andamento")
        Long emAndamento,

        Long pendentes,

        Long atrasadas,

        @JsonProperty("tarefas_prioritarias")
        List<TarefaPrioritariaResponseDTO> tarefasPrioritarias
) {
}
