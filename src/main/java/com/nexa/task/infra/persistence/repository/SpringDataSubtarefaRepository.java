package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.subtarefa.SubtarefaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSubtarefaRepository extends JpaRepository<SubtarefaEntity, Long> {
}
