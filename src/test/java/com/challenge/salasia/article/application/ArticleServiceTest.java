package com.challenge.salasia.article.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.challenge.salasia.article.api.ArticleProvider;
import com.challenge.salasia.article.api.model.AlgoliaHit;
import com.challenge.salasia.article.api.model.AlgoliaResponse;
import com.challenge.salasia.article.domain.Article;
import com.challenge.salasia.article.domain.ArticleMapper;
import com.challenge.salasia.article.domain.ArticleRepository;
import com.challenge.salasia.article.web.dto.ArticleSearchRequest;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

  @Mock ArticleProvider articleProvider;

  @Mock ArticleRepository articleRepository;

  @InjectMocks ArticleService articleService;

  @Captor ArgumentCaptor<Iterable<Article>> articlesCaptor;

  @Test
  void fetchAndSaveArticles_shouldSaveValidArticle() {
    var response = getAlgoliaResponse("ICE is getting unprecedented access to Medicaid data");

    when(articleProvider.fetchArticles()).thenReturn(response);
    when(articleRepository.findAllById(Set.of("44609680"))).thenReturn(List.of());

    articleService.fetchAndSaveArticles();

    verify(articleRepository).saveAll(articlesCaptor.capture());
    var saved = articlesCaptor.getValue();
    var list = StreamSupport.stream(saved.spliterator(), false).toList();

    assertEquals(1, list.size());
    assertEquals("44609680", list.getFirst().getObjectId());
  }

  @Test
  void fetchAndSaveArticles_shouldIgnoreArticleWithBlankTitle() {
    var response = getAlgoliaResponse("");

    when(articleProvider.fetchArticles()).thenReturn(response);

    articleService.fetchAndSaveArticles();

    verify(articleRepository, never()).saveAll(any());
  }

  @Test
  void fetchAndSaveArticles_shouldSkipAlreadyExistingArticle() {
    var response = getAlgoliaResponse("ICE is getting unprecedented access to Medicaid data");
    var existing = Article.builder().objectId("44609680").build();

    when(articleProvider.fetchArticles()).thenReturn(response);
    when(articleRepository.findAllById(Set.of("44609680"))).thenReturn(List.of(existing));

    articleService.fetchAndSaveArticles();

    verify(articleRepository, never()).saveAll(any());
  }

  @Test
  void markAsDeleted_shouldMarkAsDeletedIfExists() {
    var article = Article.builder().objectId("abc123").deleted(false).build();
    when(articleRepository.findById("abc123")).thenReturn(Optional.of(article));

    boolean result = articleService.markAsDeleted("abc123");

    assertTrue(result);
    assertTrue(article.isDeleted());
  }

  @Test
  void markAsDeleted_shouldReturnFalseIfNotFound() {
    when(articleRepository.findById("nonexistent")).thenReturn(Optional.empty());

    boolean result = articleService.markAsDeleted("nonexistent");

    assertFalse(result);
  }

  @Test
  void search_shouldDelegateToRepositoryAndEnrichArticlesWithTags() {
    var req = new ArticleSearchRequest("author", "title", "tag", "2025-07", 0, 5);
    Pageable pageable = PageRequest.of(0, 10);

    Article article = Article.builder().objectId("a1").author("author").storyTitle("title").build();

    Page<Article> pageWithoutTags = new PageImpl<>(List.of(article), pageable, 1);

    Article articleWithTags =
        Article.builder()
            .objectId("a1")
            .author("author")
            .storyTitle("title")
            .tags(List.of("tag1", "tag2"))
            .build();

    when(articleRepository.findAll(any(Specification.class), eq(pageable)))
        .thenReturn(pageWithoutTags);

    when(articleRepository.findAllWithTagsByIdIn(List.of("a1")))
        .thenReturn(List.of(articleWithTags));

    Page<Article> result = articleService.search(req, pageable);

    assertEquals(1, result.getTotalElements());
    assertEquals(List.of(articleWithTags), result.getContent());

    verify(articleRepository).findAll(any(Specification.class), eq(pageable));
    verify(articleRepository).findAllWithTagsByIdIn(List.of("a1"));
  }

  @Test
  void fetchAndSaveArticles_shouldFallbackToSaveOneByOneOnIntegrityViolation() {
    var response = getAlgoliaResponse("ICE is getting unprecedented access to Medicaid data");
    var article = ArticleMapper.toEntity(response.hits().getFirst());

    when(articleProvider.fetchArticles()).thenReturn(response);
    when(articleRepository.findAllById(Set.of("44609680"))).thenReturn(List.of());

    doThrow(new DataIntegrityViolationException("unique constraint"))
        .when(articleRepository)
        .saveAll(any());

    when(articleRepository.save(any())).thenReturn(article);

    articleService.fetchAndSaveArticles();

    verify(articleRepository).saveAll(any());
    verify(articleRepository).save(article);
  }

  private static AlgoliaResponse getAlgoliaResponse(String storyTitle) {
    var hit =
        new AlgoliaHit(
            "44609680",
            "tastyface",
            "You're kidding, right? Rural (Republican) communities have been completely ravaged by the opioid crisis.",
            Instant.parse("2025-07-18T20:49:53Z"),
            Instant.parse("2025-07-18T21:14:41Z"),
            44609547L,
            44605618L,
            storyTitle,
            "https://www.wired.com/story/ice-access-medicaid-data/",
            List.of("comment", "author_tastyface", "story_44605618"));

    return new AlgoliaResponse(List.of(hit));
  }
}
