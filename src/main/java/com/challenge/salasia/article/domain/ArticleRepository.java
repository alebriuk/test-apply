package com.challenge.salasia.article.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository
    extends JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {

  @Query("SELECT a FROM Article a LEFT JOIN FETCH a.tags WHERE a.objectId IN :ids")
  List<Article> findAllWithTagsByIdIn(@Param("ids") List<String> ids);
}
