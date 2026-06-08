package com.nexa.task.domain.builder.tag;

import com.nexa.task.domain.entity.tag.Tag;

public class TagBuilder {

    private Long id = 1L;
    private Long idUsuario = 1L;
    private String nome = "Tag Padrão";
    private Boolean ativo = true;

    public TagBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public TagBuilder comIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }

    public TagBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public TagBuilder comAtivo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public Tag build() {
        return new Tag(
                id,
                idUsuario,
                nome,
                ativo
        );
    }
}
