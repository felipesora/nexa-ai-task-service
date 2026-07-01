package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.workspace.WorkspaceEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataWorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {

    boolean existsByNomeAndIdUsuario(String nome, Long idUsuario);

    boolean existsByNomeAndIdUsuarioAndIdNot(String nome, Long idUsuario, Long id);

    Page<WorkspaceEntity> findByIdUsuario(Long idUsuario, Pageable pageable);

    Page<WorkspaceEntity> findByIdUsuarioAndNomeContainingIgnoreCase(Long idUsuario, String nome, Pageable pageable);

    List<WorkspaceEntity> findAllByIconeWorkspaceId(Long idIconeWorkspace);

    List<WorkspaceEntity> findAllByCorWorkspaceId(Long idCorWorkspace);

    @Modifying
    @Transactional
    @Query("""
    UPDATE WorkspaceEntity w
       SET w.iconeWorkspace = null
     WHERE w.iconeWorkspace.id = :idIcone
""")
    void removerIconeDosWorkspaces(Long idIcone);

    @Modifying
    @Transactional
    @Query("""
    UPDATE WorkspaceEntity w
       SET w.corWorkspace = null
     WHERE w.corWorkspace.id = :idCor
""")
    void removerCorDosWorkspaces(Long idCor);
}
