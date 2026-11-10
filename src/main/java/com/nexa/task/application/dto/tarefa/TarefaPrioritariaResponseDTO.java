package com.nexa.task.application.dto.tarefa;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;

import java.time.LocalDateTime;

@JsonPropertyOrder({ "id_tarefa", "titulo", "prioridade", "status", "data_limite", "id_workspace", "nome_workspace" })
public record TarefaPrioritariaResponseDTO(
        @JsonProperty("id_tarefa")
        Long id,

        String titulo,

        PrioridadeTarefa prioridade,

        StatusTarefa status,

        @JsonProperty("data_limite")
        LocalDateTime dataLimite,

        @JsonProperty("id_workspace")
        Long idWorkspace,

        @JsonProperty("nome_workspace")
        String nomeWorkspace
) {
}
