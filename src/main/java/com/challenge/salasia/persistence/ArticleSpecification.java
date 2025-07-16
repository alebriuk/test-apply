package com.challenge.salasia.persistence;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.time.Month;

@Slf4j
public class ArticleSpecification {

    public static Specification<Article> notDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }

    public static Specification<Article> authorLike(String author) {
        return (root, query, cb) ->
                author == null ? null : cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    public static Specification<Article> titleLike(String title) {
        return (root, query, cb) ->
                title == null ? null : cb.like(cb.lower(root.get("storyTitle")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Article> hasTag(String tag) {
        return (root, query, cb) -> {
            if (tag == null) return null;
            assert query != null;
            query.distinct(true);
            Join<Article, String> tags = root.join("tags");
            return cb.equal(tags, tag);
        };
    }

    public static Specification<Article> monthEquals(String month) {
        return (root, query, cb) -> {
            if (month == null) return null;

            try {
                Month m = Month.valueOf(month.toUpperCase()); // Ej: "july" → "JULY"
                String monthNum = String.format("%02d", m.getValue()); // Ej: 7 → "07"

                log.info("Filtrando por mes '{}', convertido a número '{}'", month, monthNum);

                Expression<String> monthExpr = cb.function("TO_CHAR", String.class,
                        root.get("createdAt"), cb.literal("MM"));
                return cb.equal(monthExpr, monthNum);
            } catch (IllegalArgumentException e) {
                log.warn("Mes inválido recibido: '{}'", month);
                return null;
            }
        };
    }
}

