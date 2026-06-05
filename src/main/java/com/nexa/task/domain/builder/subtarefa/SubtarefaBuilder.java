package com.nexa.task.domain.builder.subtarefa;

import com.nexa.task.domain.entity.subtarefa.Subtarefa;

import java.time.LocalDateTime;

public class SubtarefaBuilder {

    private Long id = 1L;
    private String titulo = "Título da subtarefa";
    private Boolean concluida = false;
    private LocalDateTime criadoEm = LocalDateTime.now();
    private LocalDateTime atualizadoEm = LocalDateTime.now();
    private Boolean ativo = true;

    public SubtarefaBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public SubtarefaBuilder comTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public SubtarefaBuilder comConcluida(Boolean concluida) {
        this.concluida = concluida;
        return this;
    }

    public SubtarefaBuilder comCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
        return this;
    }

    public SubtarefaBuilder comAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
        return this;
    }

    public SubtarefaBuilder comAtivo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public Subtarefa build() {
        return new Subtarefa(
                id,
                titulo,
                concluida,
                criadoEm,
                atualizadoEm,
                ativo
        );
    }
}
