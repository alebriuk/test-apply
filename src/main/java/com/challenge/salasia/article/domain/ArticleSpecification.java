package com.challenge.salasia.article.domain;

import jakarta.persistence.criteria.*;
import java.time.Month;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class ArticleSpecification {

  private static final String FIELD_AUTHOR = "author";
  private static final String FIELD_STORY_TITLE = "storyTitle";
  private static final String FIELD_DELETED = "deleted";
  private static final String FIELD_CREATED_AT = "createdAt";
  private static final String FIELD_TAGS = "tags";

  public static Specification<Article> notDeleted() {
    return (root, query, cb) -> cb.isFalse(root.get(FIELD_DELETED));
  }

  public static Specification<Article> authorLike(String author) {
    return (root, query, cb) -> createLikePredicate(root, cb, FIELD_AUTHOR, author.toLowerCase());
  }

  public static Specification<Article> titleLike(String title) {
    return (root, query, cb) -> createLikePredicate(root, cb, FIELD_STORY_TITLE, title);
  }

  public static Specification<Article> hasTag(String tag) {
    return (root, query, cb) -> {
      if (tag == null) return null;
      assert query != null;
      query.distinct(true);
      Join<Article, String> tags = root.join(FIELD_TAGS);
      return cb.equal(tags, tag);
    };
  }

  public static Specification<Article> monthEquals(String month) {
    return (root, query, cb) -> {
      if (month == null) return null;

      try {
        Month m = Month.valueOf(month.toUpperCase());
        String monthNum = String.format("%02d", m.getValue());

        Expression<String> monthExpr =
            cb.function("TO_CHAR", String.class, root.get(FIELD_CREATED_AT), cb.literal("MM"));
        return cb.equal(monthExpr, monthNum);
      } catch (IllegalArgumentException e) {
        log.warn("Mes inválido recibido: '{}'", month);
        return null;
      }
    };
  }

  private static Predicate createLikePredicate(
      Root<Article> root, CriteriaBuilder cb, String fieldName, String value) {
    if (value == null) {
      return null;
    }
    return cb.like(cb.lower(root.get(fieldName)), "%" + value.toLowerCase() + "%");
  }
}
