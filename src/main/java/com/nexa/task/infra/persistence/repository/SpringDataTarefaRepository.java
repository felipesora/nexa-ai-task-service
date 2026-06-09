package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.tarefa.TarefaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTarefaRepository extends JpaRepository<TarefaEntity, Long> {
}
