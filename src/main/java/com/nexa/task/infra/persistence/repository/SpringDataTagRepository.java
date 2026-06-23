package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.tag.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTagRepository extends JpaRepository<TagEntity, Long> {

    @Query("""
       SELECT t
       FROM TarefaEntity ta
       JOIN ta.tags t
       WHERE ta.id = :idTarefa
       """)
    Page<TagEntity> buscarTagsPorIdTarefa(@Param("idTarefa") Long idTarefa, Pageable pageable);

    Page<TagEntity> findByIdUsuario(Long idUsuario, Pageable pageable);

    boolean existsByNomeAndIdUsuario(String nome, Long idUsuario);

    boolean existsByNomeAndIdUsuarioAndIdNot(String nome, Long idUsuario, Long id);
}
