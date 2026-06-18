package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.subtarefa.SubtarefaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSubtarefaRepository extends JpaRepository<SubtarefaEntity, Long> {

    Page<SubtarefaEntity> findByTarefa_Id(Long idTarefa, Pageable pageable);
}
