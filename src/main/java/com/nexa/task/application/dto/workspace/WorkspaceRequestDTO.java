package com.nexa.task.application.dto.workspace;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WorkspaceRequestDTO(
        @NotNull(message = "ID do usuário é obrigatório")
        @JsonProperty("id_usuario")
        Long idUsuario,

        @NotBlank(message = "Nome do workspace é obrigatório")
        @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
        String nome,

        @Size(min = 3, max = 500, message = "A descrição deve ter entre 3 e 500 caracteres")
        String descricao,

        @JsonProperty("id_cor")
        Long idCor,

        @JsonProperty("id_icone")
        Long idIcone
) {
}
