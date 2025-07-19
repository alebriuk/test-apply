package com.challenge.salasia.article.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleScheduler {

    private final ArticleService articleService;

    @Scheduled(fixedRateString = "${scheduling.articles.rate}")
    public void scheduledFetchAndSaveArticles() {
        log.info("Scheduled execution: fetching and saving articles");
        try {
            articleService.fetchAndSaveArticles();
        } catch (Exception e) {
            log.error("Scheduled job failed", e);
        }
    }
}

