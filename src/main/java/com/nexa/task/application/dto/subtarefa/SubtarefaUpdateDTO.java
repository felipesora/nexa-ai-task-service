package com.nexa.task.application.dto.subtarefa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubtarefaUpdateDTO (
        @NotBlank(message = "Título da subtarefa é obrigatório")
        @Size(min = 3, max = 200, message = "O Título deve ter entre 3 e 200 caracteres")
        String titulo
) {
}
