package com.nexa.task.application.dto.tarefa;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nexa.task.domain.entity.tarefa.DificuldadeTarefa;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;

import java.time.LocalDateTime;

@JsonPropertyOrder({ "id_tarefa", "id_usuario", "titulo", "descricao", "prioridade",
        "status", "dificuldade", "data_limite", "data_conclusao",
        "criado_em", "atualizado_em", "ativo", "id_workspace" })
public record TarefaResponseDTO(
        @JsonProperty("id_tarefa")
        Long id,

        @JsonProperty("id_usuario")
        Long idUsuario,

        String titulo,

        String descricao,

        PrioridadeTarefa prioridade,

        StatusTarefa status,

        DificuldadeTarefa dificuldade,

        @JsonProperty("data_limite")
        LocalDateTime dataLimite,

        @JsonProperty("data_conclusao")
        LocalDateTime dataConclusao,

        @JsonProperty("criado_em")
        LocalDateTime criadoEm,

        @JsonProperty("atualizado_em")
        LocalDateTime atualizadoEm,

        Boolean ativo,

        @JsonProperty("id_workspace")
        Long idWorkspace
) {
}
