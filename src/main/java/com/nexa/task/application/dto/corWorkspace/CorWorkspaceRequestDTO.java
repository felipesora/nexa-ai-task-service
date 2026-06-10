package com.nexa.task.application.dto.corWorkspace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CorWorkspaceRequestDTO(
        @NotBlank(message = "Cor é obrigatório")
        @Size(min = 3, max = 50, message = "A cor deve ter entre 3 e 50 caracteres")
        String cor
) {
}
