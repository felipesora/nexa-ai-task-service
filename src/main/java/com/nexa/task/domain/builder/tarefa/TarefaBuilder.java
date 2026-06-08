package com.nexa.task.domain.builder.tarefa;

import com.nexa.task.domain.builder.workspace.WorkspaceBuilder;
import com.nexa.task.domain.entity.tarefa.DificuldadeTarefa;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.domain.entity.tarefa.Tarefa;
import com.nexa.task.domain.entity.workspace.Workspace;

import java.time.LocalDateTime;

public class TarefaBuilder {

    private Long id = 1L;
    private Long idUsuario = 1L;
    private String titulo = "Título da tarefa";
    private String descricao = "Descrição da tarefa";
    private PrioridadeTarefa prioridade = PrioridadeTarefa.BAIXA;
    private StatusTarefa status = StatusTarefa.EM_ANDAMENTO;
    private DificuldadeTarefa dificuldade = DificuldadeTarefa.BAIXA;
    private LocalDateTime dataLimite = LocalDateTime.now();
    private LocalDateTime dataConclusao = LocalDateTime.now();
    private LocalDateTime criadoEm = LocalDateTime.now();
    private LocalDateTime atualizadoEm = LocalDateTime.now();
    private Boolean ativo = true;
    private Workspace workspace = new WorkspaceBuilder().build();

    public TarefaBuilder comId(Long id) {
        this.id = id;
        return this;
    }

    public TarefaBuilder comIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }

    public TarefaBuilder comTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public TarefaBuilder comDescricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public TarefaBuilder comPrioridade(PrioridadeTarefa prioridade) {
        this.prioridade = prioridade;
        return this;
    }

    public TarefaBuilder comStatus(StatusTarefa status) {
        this.status = status;
        return this;
    }

    public TarefaBuilder comDificuldade(DificuldadeTarefa dificuldade) {
        this.dificuldade = dificuldade;
        return this;
    }

    public TarefaBuilder comDataLimite(LocalDateTime dataLimite) {
        this.dataLimite = dataLimite;
        return this;
    }

    public TarefaBuilder comDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
        return this;
    }

    public TarefaBuilder comCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
        return this;
    }

    public TarefaBuilder comAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
        return this;
    }

    public TarefaBuilder comAtivo(Boolean ativo) {
        this.ativo = ativo;
        return this;
    }

    public TarefaBuilder comWorkspace(Workspace workspace) {
        this.workspace = workspace;
        return this;
    }

    public Tarefa build() {
        return new Tarefa(
                id,
                idUsuario,
                titulo,
                descricao,
                prioridade,
                status,
                dificuldade,
                dataLimite,
                dataConclusao,
                criadoEm,
                atualizadoEm,
                ativo,
                workspace
        );
    }
}
