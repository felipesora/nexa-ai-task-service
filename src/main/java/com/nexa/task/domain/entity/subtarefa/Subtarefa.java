package com.nexa.task.domain.entity.subtarefa;


import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.exception.DomainException;

import java.time.LocalDateTime;

public class Subtarefa {

    private Long id;
    private Long idUsuario;
    private String titulo;
    private Boolean concluida;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private Boolean ativo;
    private Tarefa tarefa;

    public Subtarefa(Long id, Long idUsuario, String titulo, Boolean concluida, LocalDateTime criadoEm,
                     LocalDateTime atualizadoEm, Boolean ativo, Tarefa tarefa) {
        validarIdUsuario(idUsuario);
        validarTitulo(titulo);
        validarTarefa(tarefa);

        this.id = id;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.concluida = concluida;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.ativo = ativo;
        this.tarefa = tarefa;
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

    private void validarIdUsuario(Long idUsuario) {
        if (idUsuario == null || idUsuario == 0L) {
            throw new DomainException("Id do Usuário da subtarefa é obrigatório.");
        }
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new DomainException("Título da subtarefa é obrigatório.");
        }
    }

    private void validarTarefa(Tarefa tarefa) {
        if (tarefa == null) {
            throw new DomainException("Tarefa da subtarefa é obrigatório.");
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

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }
}
