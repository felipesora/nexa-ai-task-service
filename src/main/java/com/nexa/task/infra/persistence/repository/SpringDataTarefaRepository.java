package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTarefaRepository extends JpaRepository<TarefaEntity, Long> {

    Page<TarefaEntity> findByWorkspace_Id(Long idWorkspace, Pageable pageable);

    Page<TarefaEntity> findByIdUsuario(Long idUsuario, Pageable pageable);

    Page<TarefaEntity> findByIdUsuarioAndTituloContainingIgnoreCase(Long idUsuario, String titulo, Pageable pageable);
}
