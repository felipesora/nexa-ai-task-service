package com.nexa.task.infra.persistence.entity.tag;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Tags")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tag")
    private Long id;

    @Column(nullable = false, name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_cor", nullable = true)
    private CorTagEntity corTag;
}
