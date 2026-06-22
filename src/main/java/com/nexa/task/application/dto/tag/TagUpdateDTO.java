package com.nexa.task.application.dto.tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagUpdateDTO(
        @NotBlank(message = "Nome da tag é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @JsonProperty("id_cor")
        Long idCor
) {
}
