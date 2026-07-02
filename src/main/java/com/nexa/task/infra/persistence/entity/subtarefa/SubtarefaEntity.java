package com.nexa.task.infra.persistence.entity.subtarefa;

import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Subtarefas")
@NoArgsConstructor
@Getter
public class SubtarefaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subtarefa")
    private Long id;

    @Column(nullable = false, name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false)
    private Boolean concluida;

    @Column(nullable = false, name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(nullable = false, name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "id_tarefa", nullable = false)
    private TarefaEntity tarefa;

    public SubtarefaEntity(Long id, Long idUsuario, String titulo, Boolean concluida, LocalDateTime criadoEm, LocalDateTime atualizadoEm, Boolean ativo, TarefaEntity tarefa) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.titulo = titulo;
        this.concluida = concluida;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.ativo = ativo;
        this.tarefa = tarefa;
    }
}
