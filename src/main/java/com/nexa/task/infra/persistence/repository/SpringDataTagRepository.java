package com.nexa.task.infra.persistence.repository;

import com.nexa.task.infra.persistence.entity.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataTagRepository extends JpaRepository<TagEntity, Long> {
}
