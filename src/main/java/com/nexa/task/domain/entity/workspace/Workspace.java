package com.nexa.task.domain.entity.workspace;

import com.nexa.task.domain.exception.DomainException;

import java.time.LocalDateTime;

public class Workspace {

    private Long id;
    private Long idUsuario;
    private String nome;
    private String descricao;
    private LocalDateTime criadoEm;
    private Boolean ativo;
    private CorWorkspace corWorkspace;
    private IconeWorkspace iconeWorkspace;

    public Workspace(Long id, Long idUsuario, String nome, String descricao, LocalDateTime criadoEm,
                     Boolean ativo, CorWorkspace corWorkspace, IconeWorkspace iconeWorkspace) {
        validarIdUsuario(idUsuario);
        validarNome(nome);
        validarDescricao(descricao);

        this.id = id;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.descricao = descricao;
        this.criadoEm = criadoEm;
        this.ativo = ativo;
        this.corWorkspace = corWorkspace;
        this.iconeWorkspace = iconeWorkspace;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    private void validarIdUsuario(Long idUsuario) {
        if (idUsuario == null || idUsuario == 0L) {
            throw new DomainException("Id do Usuário do workspace é obrigatório.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DomainException("Nome do workspace é obrigatório.");
        }
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new DomainException("Descrição do workspace é obrigatória.");
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public CorWorkspace getCorWorkspace() {
        return corWorkspace;
    }

    public void setCorWorkspace(CorWorkspace corWorkspace) {
        this.corWorkspace = corWorkspace;
    }

    public IconeWorkspace getIconeWorkspace() {
        return iconeWorkspace;
    }

    public void setIconeWorkspace(IconeWorkspace iconeWorkspace) {
        this.iconeWorkspace = iconeWorkspace;
    }
}
