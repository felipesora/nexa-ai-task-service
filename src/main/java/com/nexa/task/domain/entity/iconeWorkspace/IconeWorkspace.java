package com.nexa.task.domain.entity.iconeWorkspace;

import com.nexa.task.domain.exception.DomainException;

public class IconeWorkspace {

    private Long id;
    private String nome;
    private String caminho;
    private Boolean ativo;

    public IconeWorkspace(Long id, String nome, String caminho, Boolean ativo) {
        validarNome(nome);
        validarCaminho(caminho);

        this.id = id;
        this.nome = nome;
        this.caminho = caminho;
        this.ativo = ativo;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DomainException("Nome do ícone é obrigatório.");
        }
    }

    private void validarCaminho(String caminho) {
        if (caminho == null || caminho.isBlank()) {
            throw new DomainException("Caminho do ícone é obrigatório.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
