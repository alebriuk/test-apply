package com.challenge.salasia.article.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ArticleSpecification.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleRepository.saveAll(List.of(
            Article.builder()
                    .objectId("1")
                    .author("Ozzy")
                    .storyTitle("crazyTrain")
                    .tags(List.of("tag1,tag2"))
                    .createdAt(Instant.parse("2025-07-18T00:00:00Z"))
                    .deleted(false)
                    .build(),
            Article.builder()
                    .objectId("2")
                    .author("Jimmy")
                    .storyTitle("Stairway to heaven")
                    .tags(List.of("tag1,tag2"))
                    .createdAt(Instant.parse("2025-07-18T00:00:00Z"))
                    .deleted(false)
                    .build())
        );
    }

    @ParameterizedTest
    @MethodSource("provideSearchFilters")
    void findAll_withAuthorAndMonth_shouldReturnCorrectArticles(String author, String month, int expectedArticles) {
        Specification<Article> spec = new ArticleSearchSpecBuilder()
                .withAuthor(author)
                .withMonth(month)
                .build();

        Page<Article> articles = articleRepository.findAll(spec, PageRequest.of(0, 4));

        assertThat(articles.getTotalElements()).isEqualTo(expectedArticles);
        if (expectedArticles > 0) {
            assertThat(articles.getContent().getFirst().getAuthor()).isEqualToIgnoringCase(author);
        }
    }

    private static Stream<Arguments> provideSearchFilters() {
        return Stream.of(
                Arguments.of("ozzy", "july", 1),
                Arguments.of("Jimmy", "july", 1),
                Arguments.of("Bob", "july", 0),
                Arguments.of("Ozzy", "august", 0)
        );
    }
}