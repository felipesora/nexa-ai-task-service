package com.nexa.task.domain.builder.workspace;

import com.nexa.task.domain.entity.workspace.Workspace;

import java.time.LocalDateTime;

public class WorkspaceBuilder {

    private Long id = 1L;
    private Long idUsuario = 1L;
    private String nome = "Nome do workspace";
    private String descricao = "Descrição do workspace";
    private LocalDateTime criadoEm = LocalDateTime.now();
    private Boolean ativo = true;

    public WorkspaceBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public WorkspaceBuilder comIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }

    public WorkspaceBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public WorkspaceBuilder comDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public WorkspaceBuilder comCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
        return this;
    }

    public WorkspaceBuilder comAtivo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public Workspace build() {
        return new Workspace(
                id,
                idUsuario,
                nome,
                descricao,
                criadoEm,
                ativo
        );
    }
}
