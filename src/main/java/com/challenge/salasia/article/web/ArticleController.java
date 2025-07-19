package com.challenge.salasia.article.web;

import com.challenge.salasia.article.application.ArticleService;
import com.challenge.salasia.article.domain.Article;
import com.challenge.salasia.article.web.dto.ArticleSearchRequest;
import com.challenge.salasia.shared.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Articles", description = "Operations related to articles")
@Slf4j
public class ArticleController {

  private final ArticleService articleService;

  @Operation(
      summary = "Search articles by filters",
      description =
          "Returns a paginated list of articles filtered by author, title, tag and month.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "List of articles retrieved successfully",
            content = @Content(schema = @Schema(implementation = PagedResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid filter parameters",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
      })
  @GetMapping
  public ResponseEntity<PagedResponse<Article>> searchArticles(
      @Valid @Parameter(description = "Search filters") ArticleSearchRequest req) {

    int page = Optional.ofNullable(req.page()).orElse(0);
    int size = Optional.ofNullable(req.size()).orElse(5);

    Pageable pageable = PageRequest.of(page, size);
    Page<Article> articlePage = articleService.search(req, pageable);

    return ResponseEntity.ok(
        new PagedResponse<>(
            articlePage.getContent(),
            articlePage.getNumber(),
            articlePage.getSize(),
            articlePage.getTotalElements(),
            articlePage.getTotalPages(),
            articlePage.isLast()));
  }

  @Operation(
      summary = "Soft delete an article by ID",
      description =
          "Marks an article as deleted (soft delete). Does not remove it from the database.",
      parameters = {@Parameter(name = "id", description = "Article ID", required = true)},
      responses = {
        @ApiResponse(responseCode = "204", description = "Article marked as deleted"),
        @ApiResponse(responseCode = "404", description = "Article not found", content = @Content)
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable String id) {
    boolean deleted = articleService.markAsDeleted(id);
    return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }

  @Operation(
      summary = "Trigger article fetch manually",
      description = "Simulates the scheduled service that fetches and stores articles every hour.",
      responses = {
        @ApiResponse(responseCode = "200", description = "Fetch and store completed successfully"),
        @ApiResponse(
            responseCode = "500",
            description = "Internal error while processing",
            content = @Content)
      })
  @PostMapping("/admin/refresh")
  public ResponseEntity<Void> fetchAndStoreArticles() {
    articleService.fetchAndSaveArticles();
    return ResponseEntity.ok().build();
  }
}
