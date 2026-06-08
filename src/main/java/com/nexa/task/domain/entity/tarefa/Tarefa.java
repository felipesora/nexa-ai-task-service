package com.nexa.task.domain.entity.tarefa;

import com.nexa.task.domain.entity.tag.Tag;
import com.nexa.task.domain.entity.workspace.Workspace;
import com.nexa.task.domain.exception.DomainException;

import java.time.LocalDateTime;
import java.util.List;

public class Tarefa {

    private Long id;
    private Long idUsuario;
    private String titulo;
    private String descricao;
    private PrioridadeTarefa prioridade;
    private StatusTarefa status;
    private DificuldadeTarefa dificuldade;
    private LocalDateTime dataLimite;
    private LocalDateTime dataConclusao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private Boolean ativo;
    private Workspace workspace;
    private List<Tag> tags;

    public Tarefa(Long id, Long idUsuario, String titulo, String descricao,
                  PrioridadeTarefa prioridade, StatusTarefa status, DificuldadeTarefa dificuldade,
                  LocalDateTime dataLimite, LocalDateTime dataConclusao, LocalDateTime criadoEm,
                  LocalDateTime atualizadoEm, Boolean ativo, Workspace workspace, List<Tag> tags) {
        validarIdUsuario(idUsuario);
        validarTitulo(titulo);
        validarDescricao(descricao);
        validarPrioridade(prioridade);
        validarStatus(status);
        validarDificuldade(dificuldade);
        validarWorkspace(workspace);

        this.id = id;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
        this.dificuldade = dificuldade;
        this.dataLimite = dataLimite;
        this.dataConclusao = dataConclusao;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.ativo = ativo;
        this.workspace = workspace;
        this.tags = tags;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void adicionarTag(Tag tag) {
        if (tag == null) {
            throw new DomainException("Tag é obrigatória.");
        }

        this.tags.add(tag);
    }

    public void removerTagPeloId(Long idTag) {
        this.tags.removeIf(tag -> tag.getId().equals(idTag));
    }

    private void validarIdUsuario(Long idUsuario) {
        if (idUsuario == null || idUsuario == 0L) {
            throw new DomainException("Id do Usuário da tarefa é obrigatório.");
        }
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new DomainException("Título da tarefa é obrigatório.");
        }
    }

    private void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new DomainException("Descrição da tarefa é obrigatória.");
        }
    }

    private void validarPrioridade(PrioridadeTarefa prioridade) {
        if (prioridade == null) {
            throw new DomainException("Prioridade da tarefa é obrigatória.");
        }
    }

    private void validarStatus(StatusTarefa status) {
        if (status == null) {
            throw new DomainException("Status da tarefa é obrigatório.");
        }
    }

    private void validarDificuldade(DificuldadeTarefa dificuldade) {
        if (dificuldade == null) {
            throw new DomainException("Dificuldade da tarefa é obrigatório.");
        }
    }

    private void validarWorkspace(Workspace workspace) {
        if (workspace == null) {
            throw new DomainException("Workspace da tarefa é obrigatório.");
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public PrioridadeTarefa getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeTarefa prioridade) {
        this.prioridade = prioridade;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public DificuldadeTarefa getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(DificuldadeTarefa dificuldade) {
        this.dificuldade = dificuldade;
    }

    public LocalDateTime getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDateTime dataLimite) {
        this.dataLimite = dataLimite;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
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

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
