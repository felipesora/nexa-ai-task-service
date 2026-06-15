package com.nexa.task.application.dto.tarefa;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexa.task.domain.entity.tarefa.DificuldadeTarefa;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TarefaCreateDTO(
        @NotNull(message = "ID do usuário é obrigatório")
        @JsonProperty("id_usuario")
        Long idUsuario,

        @NotBlank(message = "Título da tarefa é obrigatório")
        @Size(min = 3, max = 200, message = "O Título deve ter entre 3 e 200 caracteres")
        String titulo,

        @Size(min = 3, max = 700, message = "A descrição deve ter entre 3 e 700 caracteres")
        String descricao,

        @NotNull(message = "A prioridade é obrigatória")
        PrioridadeTarefa prioridade,

        DificuldadeTarefa dificuldade,

        @JsonProperty("data_limite")
        @FutureOrPresent(message = "A data limite deve ser uma data futura ou atual")
        LocalDateTime dataLimite,

        @NotNull(message = "ID do workspace é obrigatório")
        @JsonProperty("id_workspace")
        Long idWorkspace
) {
}
