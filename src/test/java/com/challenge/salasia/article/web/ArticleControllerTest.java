package com.challenge.salasia.article.web;

import com.challenge.salasia.article.application.ArticleService;
import com.challenge.salasia.article.config.NoSecurityConfig;
import com.challenge.salasia.article.domain.Article;
import com.challenge.salasia.auth.JwtAuthFilter;
import com.challenge.salasia.auth.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtUtil.class)
        }
)
@Import(NoSecurityConfig.class)
class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ArticleService articleService;

    @Test
    void searchArticles_shouldReturn200WithResults() throws Exception {
        var article = Article.builder().objectId("123").storyTitle("Test Title").build();
        var page = new PageImpl<>(List.of(article), PageRequest.of(0, 5), 1);

        when(articleService.search(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/articles")
                        .param("author", "john")
                        .param("title", "test")
                        .param("tag", "java")
                        .param("month", "july")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].objectId").value("123"))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void searchArticles_shouldReturn400IfMonthIsInvalid() throws Exception {
        mockMvc.perform(get("/api/articles")
                        .param("month", "2025-07"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors[0].field").value("month"))
                .andExpect(jsonPath("$.errors[0].message").value("Invalid month. Must be in English (e.g. 'july')"));
    }

    @Test
    void deleteArticle_shouldReturn204IfDeleted() throws Exception {
        when(articleService.markAsDeleted("123")).thenReturn(true);

        mockMvc.perform(delete("/api/articles/123"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteArticle_shouldReturn404IfNotFound() throws Exception {
        when(articleService.markAsDeleted("not-found")).thenReturn(false);

        mockMvc.perform(delete("/api/articles/not-found"))
                .andExpect(status().isNotFound());
    }
}