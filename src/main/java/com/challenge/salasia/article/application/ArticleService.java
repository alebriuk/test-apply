package com.challenge.salasia.article.application;

import com.challenge.salasia.article.api.ArticleProvider;
import com.challenge.salasia.article.api.model.AlgoliaResponse;
import com.challenge.salasia.article.domain.Article;
import com.challenge.salasia.article.domain.ArticleMapper;
import com.challenge.salasia.article.domain.ArticleRepository;
import com.challenge.salasia.article.domain.ArticleSearchSpecBuilder;
import com.challenge.salasia.article.web.dto.ArticleSearchRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

  private final ArticleProvider articleProvider;
  private final ArticleRepository articleRepository;

  @Transactional
  public void fetchAndSaveArticles() {
    var response = articleProvider.fetchArticles();
    var newArticles = processNewArticles(response);

    if (!newArticles.isEmpty()) {
      log.info("Found {} new articles", newArticles.size());
      saveBatch(newArticles.values().stream().toList());
      log.info("{} new articles saved successfully", newArticles.size());
    } else {
      log.info("No new articles to save");
    }
  }

  public Page<Article> search(ArticleSearchRequest req, Pageable pageable) {
    log.info(
        "Searching articles with author={}, title={}, tag={}, month={}",
        req.author(),
        req.title(),
        req.tag(),
        req.month());

    Specification<Article> spec =
        new ArticleSearchSpecBuilder()
            .withAuthor(req.author())
            .withTitle(req.title())
            .withTag(req.tag())
            .withMonth(req.month())
            .build();

    return articleRepository.findAll(spec, pageable);
  }

  @Transactional
  public boolean markAsDeleted(String id) {
    return articleRepository
        .findById(id)
        .map(
            article -> {
              article.setDeleted(true);
              return true;
            })
        .orElse(false);
  }

  private Map<String, Article> processNewArticles(AlgoliaResponse response) {
    List<Article> validArticles =
        response.hits().stream()
            .map(ArticleMapper::toEntity)
            .filter(Objects::nonNull)
            .filter(this::isValid)
            .toList();

    int discarded = response.hits().size() - validArticles.size();
    if (discarded > 0) {
      log.info("{} invalid articles discarded", discarded);
    }

    Map<String, Article> newArticles =
        validArticles.stream()
            .collect(
                Collectors.toMap(
                    Article::getObjectId,
                    Function.identity(),
                    (existing, replacement) -> existing));

    if (newArticles.isEmpty()) return newArticles;

    Set<String> existingIds =
        articleRepository.findAllById(newArticles.keySet()).stream()
            .map(Article::getObjectId)
            .collect(Collectors.toSet());

    log.info("{} new articles found", newArticles.size());
    return newArticles.entrySet().stream()
        .filter(entry -> !existingIds.contains(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private void saveBatch(List<Article> batch) {
    try {
      articleRepository.saveAll(batch);
    } catch (DataIntegrityViolationException e) {
      log.warn("Batch save failed due to integrity violation, saving one by one", e);
      saveOneByOne(batch);
    }
  }

  private void saveOneByOne(List<Article> batch) {
    for (Article article : batch) {
      try {
        articleRepository.save(article);
      } catch (Exception e) {
        log.error("Error saving article {}: {}",
                article.getObjectId(), e.getMessage());
      }
    }
  }

  private boolean isValid(Article article) {
    return article.getObjectId() != null
        && !article.getObjectId().isBlank()
        && article.getStoryTitle() != null
        && !article.getStoryTitle().isBlank();
  }
}
