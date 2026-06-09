package com.nexa.task.application.dto.iconeWorkspace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IconeWorkspaceRequestDTO(
        @NotBlank(message = "Nome do ícone é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "Caminho do ícone é obrigatório")
        @Size(min = 3, max = 255, message = "O caminho deve ter entre 3 e 255 caracteres")
        String caminho
) {
}
