package com.nexa.task.domain.repository;

import com.nexa.task.domain.entity.tag.CorTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CorTagRepository {

    CorTag save(CorTag cor);

    Page<CorTag> findAll(Pageable pageable);

    Optional<CorTag> findById(Long id);

    Optional<CorTag> findByCor(String cor);

    Optional<CorTag> findByIdAtivo(Long id);
}
