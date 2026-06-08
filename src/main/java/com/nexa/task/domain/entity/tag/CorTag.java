package com.nexa.task.domain.entity.tag;

import com.nexa.task.domain.exception.DomainException;

public class CorTag {

    private Long id;
    private String cor;
    private Boolean ativo;

    public CorTag(Long id, String cor, Boolean ativo) {
        validarCor(cor);

        this.id = id;
        this.cor = cor;
        this.ativo = ativo;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    private void validarCor(String cor) {
        if (cor == null || cor.isBlank()) {
            throw new DomainException("Cor da tag é obrigatório.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
