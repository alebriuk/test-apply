package com.challenge.salasia.article.api;

import com.challenge.salasia.article.api.model.AlgoliaResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlgoliaArticleProvider implements ArticleProvider {

  private final RestClient algoliaRestClient;

  @Override
  public AlgoliaResponse fetchArticles() {
    try {
      return algoliaRestClient
          .get()
          .uri(uriBuilder -> uriBuilder.path("/search_by_date").queryParam("query", "java").build())
          .retrieve()
          .body(AlgoliaResponse.class);
    } catch (Exception e) {
      log.error("Error when calling Algolia API", e);
      return new AlgoliaResponse(List.of());
    }
  }
}
