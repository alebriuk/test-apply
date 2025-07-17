package com.challenge.salasia.article.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArticleRepository
    extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

  @EntityGraph(attributePaths = "tags")
  Page<Article> findAll(Specification<Article> spec, Pageable pageable);
}
