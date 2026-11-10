package com.nexa.task.infra.persistence.repository;

import com.nexa.task.domain.entity.tarefa.StatusTarefa;
import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataTarefaRepository extends JpaRepository<TarefaEntity, Long> {

    Page<TarefaEntity> findByWorkspace_Id(Long idWorkspace, Pageable pageable);

    Page<TarefaEntity> findByWorkspace_IdAndAtivoTrue(Long idWorkspace, Pageable pageable);

    long countByWorkspace_IdAndAtivoTrue(Long idWorkspace);

    Page<TarefaEntity> findByIdUsuario(Long idUsuario, Pageable pageable);

    Page<TarefaEntity> findByIdUsuarioAndAtivoTrue(Long idUsuario, Pageable pageable);

    Page<TarefaEntity> findByIdUsuarioAndTituloContainingIgnoreCase(Long idUsuario, String titulo, Pageable pageable);

    Page<TarefaEntity> findByIdUsuarioAndTituloContainingIgnoreCaseAndAtivoTrue(Long idUsuario, String titulo, Pageable pageable);

    List<TarefaEntity> findAllByWorkspace_Id(Long idWorkspace);

    Optional<TarefaEntity> findByIdAndAtivoTrue(Long id);

    long countByIdUsuarioAndStatusAndAtivoTrue(Long idUsuario, StatusTarefa status);

    // Tarefas em aberto da maior para a menor prioridade; no empate, prazo mais próximo primeiro (sem prazo por último)
    @Query("""
            SELECT t FROM TarefaEntity t
            WHERE t.idUsuario = :idUsuario
              AND t.ativo = true
              AND t.status <> com.nexa.task.domain.entity.tarefa.StatusTarefa.CONCLUIDA
            ORDER BY
              CASE t.prioridade
                WHEN com.nexa.task.domain.entity.tarefa.PrioridadeTarefa.URGENTE THEN 0
                WHEN com.nexa.task.domain.entity.tarefa.PrioridadeTarefa.ALTA THEN 1
                WHEN com.nexa.task.domain.entity.tarefa.PrioridadeTarefa.MEDIA THEN 2
                ELSE 3
              END,
              CASE WHEN t.dataLimite IS NULL THEN 1 ELSE 0 END,
              t.dataLimite ASC
            """)
    List<TarefaEntity> buscarPrioritariasPorIdUsuario(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
