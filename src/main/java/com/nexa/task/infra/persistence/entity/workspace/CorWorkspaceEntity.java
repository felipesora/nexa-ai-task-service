package com.nexa.task.infra.persistence.entity.workspace;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Cores_Workspace")
@NoArgsConstructor
@Getter
public class CorWorkspaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cor")
    private Long id;

    @Column(nullable = false, length = 50)
    private String cor;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "corWorkspace", fetch = FetchType.LAZY)
    private Set<WorkspaceEntity> workspaces = new HashSet<>();

    public CorWorkspaceEntity(Long id, String cor, Boolean ativo) {
        this.id = id;
        this.cor = cor;
        this.ativo = ativo;
    }
}
