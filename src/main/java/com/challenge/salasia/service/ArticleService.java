package com.challenge.salasia.service;

import com.challenge.salasia.api.model.AlgoliaResponse;
import com.challenge.salasia.mapper.ArticleMapper;
import com.challenge.salasia.persistence.Article;
import com.challenge.salasia.persistence.ArticleRepository;
import com.challenge.salasia.persistence.ArticleSearchSpecBuilder;
import com.challenge.salasia.web.dto.ArticleSearchRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final RestClient algoliaRestClient;
    private final ArticleRepository articleRepository;

    public AlgoliaResponse fetchJavaArticles() {
        try {
            return algoliaRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search_by_date")
                            .queryParam("query", "java")
                            .build())
                    .retrieve()
                    .body(AlgoliaResponse.class);
        } catch (Exception e) {
            log.error("Error when calling Algolia API", e);
            return new AlgoliaResponse(java.util.List.of());
        }
    }

    @Transactional
    public void fetchAndSaveArticles() {
        var response = fetchJavaArticles();

        var nuevos = response.hits().stream()
                .map(ArticleMapper::toEntity)
                .collect(Collectors.toMap(Article::getObjectId, a -> a, (a1, a2) -> a1)); // deduplica por ID

        var existentes = articleRepository.findAllById(nuevos.keySet())
                .stream()
                .map(Article::getObjectId)
                .collect(Collectors.toSet());

        nuevos.keySet().removeAll(existentes);

        var aGuardar = new ArrayList<>(nuevos.values());

        articleRepository.saveAll(aGuardar);
        log.info("Se guardaron {} artículos nuevos", aGuardar.size());
    }

    public Page<Article> search(ArticleSearchRequest req, Pageable pageable) {
        log.info("Buscando artículos con author={}, title={}, tag={}, month={}",
                req.author(), req.title(), req.tag(), req.month());

        Specification<Article> spec = new ArticleSearchSpecBuilder()
                .withAuthor(req.author())
                .withTitle(req.title())
                .withTag(req.tag())
                .withMonth(req.month())
                .build();

        return articleRepository.findAll(spec, pageable);
    }

    @Transactional
    public void markAsDeleted(String id) {
        articleRepository.findById(id).ifPresent(article -> {
            article.setDeleted(true);
//            articleRepository.save(article);
        });
    }
}
