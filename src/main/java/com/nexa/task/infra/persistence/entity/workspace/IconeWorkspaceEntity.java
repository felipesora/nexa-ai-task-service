package com.nexa.task.infra.persistence.entity.workspace;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Icones_Workspace")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class IconeWorkspaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_icone")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 255)
    private String caminho;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "iconeWorkspace", fetch = FetchType.LAZY)
    private Set<WorkspaceEntity> workspaces = new HashSet<>();
}
