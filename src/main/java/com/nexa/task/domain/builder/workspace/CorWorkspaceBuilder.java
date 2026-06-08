package com.nexa.task.domain.builder.workspace;

import com.nexa.task.domain.entity.workspace.CorWorkspace;

public class CorWorkspaceBuilder {

    private Long id = 1L;
    private String cor = "#FFFFFF";
    private Boolean ativo = true;

    public CorWorkspaceBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public CorWorkspaceBuilder comCor(String cor) {
        this.cor = cor;
        return this;
    }

    public CorWorkspaceBuilder comAtivo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public CorWorkspace build() {
        return new CorWorkspace(
                id,
                cor,
                ativo
        );
    }
}
