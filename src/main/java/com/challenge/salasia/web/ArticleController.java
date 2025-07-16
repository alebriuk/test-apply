package com.challenge.salasia.web;

import com.challenge.salasia.persistence.Article;
import com.challenge.salasia.service.ArticleService;
import com.challenge.salasia.web.dto.ArticleSearchRequest;
import com.challenge.salasia.web.dto.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<PagedResponse<Article>> searchArticles(
            @Valid @ModelAttribute ArticleSearchRequest req) {

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
                        articlePage.isLast()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String id) {
        articleService.markAsDeleted(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/fetch")
    public ResponseEntity<Void> fetchAndStoreArticles() {
        articleService.fetchAndSaveArticles();
        return ResponseEntity.ok().build();
    }
}
