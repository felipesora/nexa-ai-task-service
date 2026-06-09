package com.nexa.task.infra.persistence.entity.tarefa;

import com.nexa.task.domain.entity.tarefa.DificuldadeTarefa;
import com.nexa.task.domain.entity.tarefa.PrioridadeTarefa;
import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.infra.persistence.entity.subtarefa.SubtarefaEntity;
import com.nexa.task.infra.persistence.entity.tag.TagEntity;
import com.nexa.task.infra.persistence.entity.workspace.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Tarefas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TarefaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarefa")
    private Long id;

    @Column(nullable = false, name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 700)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeTarefa prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DificuldadeTarefa dificuldade;

    @Column(nullable = false, name = "data_limite")
    private LocalDateTime dataLimite;

    @Column(nullable = false, name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(nullable = false, name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(nullable = false, name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_workspace", nullable = false)
    private WorkspaceEntity workspace;

    @OneToMany(mappedBy = "tarefa", fetch = FetchType.LAZY)
    private Set<SubtarefaEntity> subtarefas = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Tarefas_tags",
            joinColumns = @JoinColumn(name = "id_tarefa"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    private Set<TagEntity> tags = new HashSet<>();
}
