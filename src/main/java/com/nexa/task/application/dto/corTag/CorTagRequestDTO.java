package com.nexa.task.application.dto.corTag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CorTagRequestDTO(
        @NotBlank(message = "Cor é obrigatório")
        @Size(min = 3, max = 50, message = "A cor deve ter entre 3 e 50 caracteres")
        String cor
) {
}
