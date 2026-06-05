package com.nexa.task.domain.entity.subtarefa;


import com.nexa.task.domain.exception.DomainException;

import java.time.LocalDateTime;

public class Subtarefa {

    private Long id;
    private String titulo;
    private Boolean concluida;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private Boolean ativo;

    public Subtarefa(Long id, String titulo, Boolean concluida, LocalDateTime criadoEm, LocalDateTime atualizadoEm, Boolean ativo) {
        validarTitulo(titulo);

        this.id = id;
        this.titulo = titulo;
        this.concluida = concluida;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.ativo = ativo;
    }

    public void marcarConcluida() {
        this.concluida = true;
    }

    public void desmarcarConcluida() {
        this.concluida = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new DomainException("Título da subtarefa é obrigatório.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
