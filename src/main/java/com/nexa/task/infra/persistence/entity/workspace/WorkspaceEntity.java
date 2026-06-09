package com.nexa.task.infra.persistence.entity.workspace;

import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Workspaces")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WorkspaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_workspace")
    private Long id;

    @Column(nullable = false, name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false, name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(nullable = false, name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_cor", nullable = true)
    private CorWorkspaceEntity corWorkspace;

    @ManyToOne
    @JoinColumn(name = "id_icone", nullable = true)
    private IconeWorkspaceEntity iconeWorkspace;

    @OneToMany(mappedBy = "workspace", fetch = FetchType.LAZY)
    private Set<TarefaEntity> tarefas = new HashSet<>();
}
