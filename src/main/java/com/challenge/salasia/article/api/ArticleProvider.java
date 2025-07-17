package com.challenge.salasia.article.api;

import com.challenge.salasia.article.api.model.AlgoliaResponse;

public interface ArticleProvider {
  AlgoliaResponse fetchArticles();
}
