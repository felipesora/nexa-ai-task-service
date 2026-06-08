package com.nexa.task.domain.builder.workspace;

import com.nexa.task.domain.entity.workspace.IconeWorkspace;

public class IconeWorkspaceBuilder {

    private Long id = 1L;
    private String nome = "Ícone Padrão";
    private String caminho = "padrao.png";
    private Boolean ativo = true;

    public IconeWorkspaceBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public IconeWorkspaceBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public IconeWorkspaceBuilder comCaminho(String caminho) {
        this.caminho = caminho;
        return this;
    }

    public IconeWorkspaceBuilder comAtivo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public IconeWorkspace build() {
        return new IconeWorkspace(
                id,
                nome,
                caminho,
                ativo
        );
    }
}
