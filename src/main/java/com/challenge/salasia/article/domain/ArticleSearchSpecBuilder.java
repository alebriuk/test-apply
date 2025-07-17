package com.challenge.salasia.article.domain;

import org.springframework.data.jpa.domain.Specification;

public class ArticleSearchSpecBuilder {

  private Specification<Article> spec = ArticleSpecification.notDeleted();

  public ArticleSearchSpecBuilder withAuthor(String author) {
    if (author != null && !author.isBlank()) {
      spec = spec.and(ArticleSpecification.authorLike(author.trim()));
    }
    return this;
  }

  public ArticleSearchSpecBuilder withTitle(String title) {
    if (title != null && !title.isBlank()) {
      spec = spec.and(ArticleSpecification.titleLike(title.trim()));
    }
    return this;
  }

  public ArticleSearchSpecBuilder withTag(String tag) {
    if (tag != null && !tag.isBlank()) {
      spec = spec.and(ArticleSpecification.hasTag(tag.trim()));
    }
    return this;
  }

  public ArticleSearchSpecBuilder withMonth(String month) {
    if (month != null && !month.isBlank()) {
      spec = spec.and(ArticleSpecification.monthEquals(month.trim()));
    }
    return this;
  }

  public Specification<Article> build() {
    return spec;
  }
}
