package com.nexa.task.infra.persistence.entity.tag;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Cores_Tag")
@NoArgsConstructor
@Getter
public class CorTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cor")
    private Long id;

    @Column(nullable = false, length = 50)
    private String cor;

    @Column(nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "corTag", fetch = FetchType.LAZY)
    private Set<TagEntity> tags = new HashSet<>();

    public CorTagEntity(Long id, String cor, Boolean ativo) {
        this.id = id;
        this.cor = cor;
        this.ativo = ativo;
    }
}
