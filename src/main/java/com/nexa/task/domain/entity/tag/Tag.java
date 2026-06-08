package com.nexa.task.domain.entity.tag;

import com.nexa.task.domain.exception.DomainException;

public class Tag {

    private Long id;
    private Long idUsuario;
    private String nome;
    private Boolean ativo;
    private CorTag corTag;

    public Tag(Long id, Long idUsuario, String nome, Boolean ativo, CorTag corTag) {
        validarIdUsuario(idUsuario);
        validarNome(nome);

        this.id = id;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.ativo = ativo;
        this.corTag = corTag;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    private void validarIdUsuario(Long idUsuario) {
        if (idUsuario == null || idUsuario == 0L) {
            throw new DomainException("Id do Usuário da tag é obrigatório.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DomainException("Nome da tag é obrigatório.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public CorTag getCorTag() {
        return corTag;
    }

    public void setCorTag(CorTag corTag) {
        this.corTag = corTag;
    }
}
