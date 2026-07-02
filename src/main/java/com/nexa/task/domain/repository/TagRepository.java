package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.tag.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TagRepository {

    Tag save(Tag tag);

    Page<Tag> findAll(Pageable pageable);

    Page<Tag> findByIdTarefa(Long idTarefa, Pageable pageable);

    Page<Tag> findByIdUsuario(Long idUsuario, Pageable pageable);

    Optional<Tag> findById(Long id);

    boolean existsByNomeAndIdUsuario(String nome, Long idUsuario);

    boolean existsByNomeAndIdUsuarioAndIdNot(String nome, Long idUsuario, Long id);

    void removerCorDasTags(Long idCor);
}
