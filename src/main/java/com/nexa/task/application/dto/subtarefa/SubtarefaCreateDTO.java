package com.nexa.task.application.dto.subtarefa;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubtarefaCreateDTO(
        @NotBlank(message = "Título da subtarefa é obrigatório")
        @Size(min = 3, max = 200, message = "O Título deve ter entre 3 e 200 caracteres")
        String titulo,

        @NotNull(message = "ID da tarefa é obrigatório")
        @JsonProperty("id_tarefa")
        Long idTarefa
) {
}
